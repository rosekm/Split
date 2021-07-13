package models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dbModel.Branch;
import dbModel.Customer;
import dbModel.Salesman;
import dbUtils.BranchDAO;
import dbUtils.CustomerDAO;
import dbUtils.SalesmanDAO;
import fxmodel.BranchFX;
import fxmodel.CustomerFX;
import fxmodel.SalesmanFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.BranchFXConverter;
import utils.CustomerFXConverter;
import utils.SalesmanFXConverter;
import utils.UserManager;

/*Manages communication between DB and Salesman Maintenance View area of User's view
*/
public class SalesmenModel {

	private BranchDAO branchDAO;
	private SalesmanDAO salesmenDAO;
	private CustomerDAO customerDAO;
	private ObjectProperty<SalesmanFX> salesmanObjProp;
	private SalesmanFX salesmanObj;
	private ObservableList<SalesmanFX> salesmanFXList;
	private ArrayList<BranchFX> branchFXList;
	private String userType;

	public SalesmenModel() {
		this.branchDAO = new BranchDAO();
		this.salesmenDAO = new SalesmanDAO();
		this.customerDAO = new CustomerDAO();
		this.salesmanObj = new SalesmanFX();
		this.salesmanObjProp = new SimpleObjectProperty<SalesmanFX>(new SalesmanFX());
		this.userType = UserManager.getActiveUserType();
		this.salesmanFXList = FXCollections.observableArrayList();
	}

	public ObservableList<SalesmanFX> getSalesmanList() {
		return salesmanFXList;
	}

	public ArrayList<BranchFX> getBranchList() {
		return branchFXList;
	}

	public void queryForSalesman() {
		this.salesmanFXList.clear();
		String salesmanName = salesmanObj.getNameProperty().isEmpty().get() ? "%"
				: salesmanObj.getNameProperty().getValue();
		String salesmanSurname = salesmanObj.getSurnameProperty().isEmpty().get() ? "%"
				: salesmanObj.getSurnameProperty().getValue();
		String salesmanBranch = salesmanObj.getBranchObjectProperty().isNull().get() ? "%"
				: salesmanObj.getBranchObjectProperty().getValue().getName();
		queryForBranches();
		ArrayList<Salesman> salesmanList = salesmenDAO.getQueryResults(salesmanName, salesmanSurname, salesmanBranch);

		salesmanList.stream().forEach(salesman -> {
			for (BranchFX branch : branchFXList) {
				if (branch.getName().equals(salesman.getBranch().getName())) {
					salesmanFXList.add(SalesmanFXConverter.convertToSalesmanFX(salesman));
					break;
				}
			}
		});
	}

	public ArrayList<BranchFX> queryForBranches() {
		ArrayList<Branch> branchList = userType.equals("ADMIN") ? branchDAO.queryForAll()
				: UserManager.getActiveUserAuthorizations();
		branchFXList = (ArrayList<BranchFX>) branchList.stream()
				.map(branch -> BranchFXConverter.convertToBranchFX(branch)).collect(Collectors.toList());
		branchFXList.add(0, new BranchFX(""));
		return branchFXList;
	}

	public int deleteSalesmanFromDB() {
		int result = salesmenDAO.deleteRecord(SalesmanFXConverter.convertToSalesman(salesmanObjProp.get()));
		if (result == 1) {
			this.salesmanFXList.remove(salesmanObjProp.get());
		}
		return result;
	}

	public int updateSalesmanData() {
		return salesmenDAO.updateRecord(SalesmanFXConverter.convertToSalesman(salesmanObjProp.get()));
	}

	public int addSalesmanData() {
		this.salesmanFXList.clear();
		return salesmenDAO.addRecord(SalesmanFXConverter.convertToSalesman(salesmanObjProp.get()));
	}

	public ArrayList<CustomerFX> getCustomersAttchedToSalesman() {
		ArrayList<Customer> salesmanCustomer = customerDAO.getQueryResults(null, null,
				salesmanObjProp.get().getIdProperty().get());
		ArrayList<CustomerFX> salesmansCustomerFX = (ArrayList<CustomerFX>) salesmanCustomer.stream()
				.map(customer -> CustomerFXConverter.convertToCustomerFX(customer)).collect(Collectors.toList());
		return salesmansCustomerFX;
	}

	public SalesmanFX getSalesmanObj() {
		return salesmanObj;
	}

	public ObjectProperty<SalesmanFX> getSalesmanObjProp() {
		return salesmanObjProp;
	}

	public void setSalesmanObjProp(SalesmanFX salesman) {
		this.salesmanObjProp.set(salesman);
	}

}
