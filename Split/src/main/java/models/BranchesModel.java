package models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dbModel.Branch;
import dbModel.Salesman;
import dbUtils.BranchDAO;
import dbUtils.SalesmanDAO;
import fxmodel.BranchFX;
import fxmodel.SalesmanFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.BranchFXConverter;
import utils.SalesmanFXConverter;

/*Manages communication between DB and Branches Maintenance View area of User's view
*/
public class BranchesModel {

	private ObjectProperty<BranchFX> branchObjProp;
	private BranchFX branchObj;
	private ObservableList<BranchFX> branchFXList;
	private BranchDAO branchDAO;
	private SalesmanDAO salesmanDAO;

	public BranchesModel() {
		this.branchDAO = new BranchDAO();
		this.salesmanDAO = new SalesmanDAO();
		this.branchObjProp = new SimpleObjectProperty<BranchFX>();
		this.branchObj = new BranchFX();
		setBranchObjProp(branchObj);
		this.branchFXList = FXCollections.observableArrayList();
		
	}

	public ObservableList<BranchFX> getBranchList() {
		return branchFXList;
	}

	public int addBranchData() {
		int result = branchDAO.addRecord(BranchFXConverter.convertToBranch(branchObjProp.getValue())); 
		if(result == 1) {
			this.branchFXList.add(this.branchObjProp.getValue());
		}
		return result;
	}

	public int deleteBranchFromDB() {
		int result = branchDAO.deleteRecord(BranchFXConverter.convertToBranch(branchObjProp.getValue()));
		if (result == 1) {
			this.branchFXList.remove(this.branchObjProp.getValue());
		}
		return result;
	}

	public void queryForAllBranches() {
		branchFXList.clear();
		ArrayList<Branch> salesmanList = branchDAO.queryForAll();
		salesmanList.stream().forEach(branch -> branchFXList.add(BranchFXConverter.convertToBranchFX(branch)));
	}

	public ArrayList<SalesmanFX> getSalesmenListForBranch() {
		ArrayList<Salesman> salesmanList = salesmanDAO.getQueryResults("%", "%", branchObjProp.getValue().getName());
		ArrayList<SalesmanFX> salesmanFXList = (ArrayList<SalesmanFX>) salesmanList.stream()
				.map(salesman -> SalesmanFXConverter.convertToSalesmanFX(salesman)).collect(Collectors.toList());
		return salesmanFXList;
	}

	public BranchFX getBranchObj() {
		return branchObj;
	}

	public void setBranchObjProp(BranchFX branch) {
		this.branchObjProp.set(branch);
	}
}
