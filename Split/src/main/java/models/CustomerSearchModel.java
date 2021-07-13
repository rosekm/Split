package models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dbModel.Customer;
import dbModel.Salesman;
import dbUtils.CustomerDAO;
import dbUtils.SalesmanDAO;
import fxmodel.CustomerFX;
import fxmodel.SalesmanFX;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.CustomerFXConverter;
import utils.SalesmanFXConverter;

/*Manages communication between DB and Customer Maintenance View area of User's view
*/
public class CustomerSearchModel {

	private CustomerDAO customerDao;
	private SalesmanDAO salesmanDao;
	private ObjectProperty<CustomerFX> customerFX;
	private ObservableList<CustomerFX> customerFXList;
	private ArrayList<SalesmanFX> salesmenFXList;

	public CustomerSearchModel() {
		customerFX = new SimpleObjectProperty<CustomerFX>(new CustomerFX());
		customerDao = new CustomerDAO();
		salesmanDao = new SalesmanDAO();
		customerFXList = FXCollections.observableArrayList();
	}

	public void queryForCustomers() {
		customerFXList.clear();
		String searchedCustomerName = customerFX.getValue().getNameProperty().isEmpty().get() ? "%"
				: customerFX.getValue().getNameProperty().getValue();
		String searchedVatNo = customerFX.getValue().getVatNoProperty().isEmpty().get() ? "%"
				: customerFX.getValue().getVatNoProperty().getValue();
		Integer searchedSalesmanID = customerFX.getValue().getSalesmanProperty().getValue() == null ? -1
				: customerFX.getValue().getSalesmanProperty().getValue().getIdProperty().get();

		ArrayList<Customer> queryResult = customerDao.getQueryResults(searchedVatNo, searchedCustomerName,
				searchedSalesmanID);
		customerFXList.addAll((ArrayList<CustomerFX>) queryResult.stream()
				.map(customer -> CustomerFXConverter.convertToCustomerFX(customer)).collect(Collectors.toList()));
	}

	public ArrayList<SalesmanFX> queryForSalesman() {
		ArrayList<Salesman> queryResults = salesmanDao.queryForAll();
		salesmenFXList = (ArrayList<SalesmanFX>) queryResults.stream()
				.map(salesman -> SalesmanFXConverter.convertToSalesmanFX(salesman)).collect(Collectors.toList());
		salesmenFXList.add(0, new SalesmanFX("", ""));
		return salesmenFXList;
	}

	public int deleteCustomerFromDB() {
		int deletionResult = this.customerDao
				.deleteRecord(CustomerFXConverter.convertToCustomer(customerFX.getValue()));
		if (deletionResult == 1) {
			this.customerFXList.remove(customerFX.getValue());
		}
		return deletionResult;
	}

	public int updateCustomerData() {
		return this.customerDao.updateRecord(CustomerFXConverter.convertToCustomer(customerFX.getValue()));
	}

	public CustomerFX getCustomerFX() {
		return customerFX.getValue();
	}

	public void setCustomerFX(CustomerFX customerFX) {
		this.customerFX.setValue(customerFX);
	}

	public ObservableList<CustomerFX> getCustomerList() {
		return customerFXList;
	}

	public ArrayList<SalesmanFX> getSalesmenList() {
		return salesmenFXList;
	}

}
