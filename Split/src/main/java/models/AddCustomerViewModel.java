package models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import birUtils.BIRSessionManager;
import dbModel.Salesman;
import dbUtils.CustomerDAO;
import dbUtils.SalesmanDAO;
import fxmodel.CustomerFX;
import fxmodel.SalesmanFX;
import utils.CustomerFXConverter;
import utils.SalesmanFXConverter;

/*Manages communication between DB and Customer Adding View area of User's view
*/
public class AddCustomerViewModel {

	private CustomerFX addedCustomerFX;
	private CustomerDAO customerDao;
	private SalesmanDAO salesmanDao;
	private ArrayList<SalesmanFX> salesmenList;
	private BIRSessionManager birSessionManager;
	
	public AddCustomerViewModel() {
		this.addedCustomerFX = new CustomerFX();
		this.customerDao = new CustomerDAO();
		this.salesmanDao = new SalesmanDAO();
		this.birSessionManager = new BIRSessionManager();
	}

	public CustomerFX getAddedCustomerFX() {
		return addedCustomerFX;
	}
	
	/**
	 * triggers upload of single customer data into database
	 * @return: number of updated records (0 if record exists already or 1) or -1 in case of error while uploading data
	 *
	 */
	public int saveNewCustomerInDB() {
			return customerDao.addRecord(CustomerFXConverter.convertToCustomer(addedCustomerFX));
	}
	
	public void queryForSalesman() {
		ArrayList<Salesman> queryResults = salesmanDao.queryForAll();
		this.salesmenList = (ArrayList<SalesmanFX>) queryResults.stream()
				.map(salesman -> SalesmanFXConverter.convertToSalesmanFX(salesman)).collect(Collectors.toList());
	}

	public ArrayList<SalesmanFX> getSalesmenList() {
		queryForSalesman();
		return salesmenList;
	}
	
	public void getBIRCustomer() {
		addedCustomerFX = birSessionManager.getBIRCustomer(addedCustomerFX);
	}
}
