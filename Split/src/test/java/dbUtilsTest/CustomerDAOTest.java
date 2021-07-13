package dbUtilsTest;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dbModel.Branch;
import dbModel.Customer;
import dbModel.DBStatement;
import dbModel.Salesman;
import dbUtils.BranchDAO;
import dbUtils.CustomerDAO;
import dbUtils.SalesmanDAO;
import utils.DialogUtils;

public class CustomerDAOTest {

	Customer customer;
	Salesman salesman;
	Branch branch;
	
	CustomerDAO customerDao;
	SalesmanDAO salesmanDao;
	BranchDAO branchDao;
	List<Customer> results;
	
	@Before
	public void setUp() throws Exception {
		customerDao = new CustomerDAO();
		salesmanDao = new SalesmanDAO();
		branchDao = new BranchDAO();
		
	}


	@Test
	public void shouldReturn1WhenAddingUniqueRecordToDB() {
		createCustomer();
		
		assertEquals(1, customerDao.addRecord(customer));
	}
	
	@Test 
	public void shouldReturn0WhenAddingAlreadyExistingRecordToDB() {
		createCustomer();
		
		customerDao.addRecord(customer);
		
		assertEquals(0, customerDao.addRecord(customer));
	}
	
	@Test
	public void shouldReturnExactRecordWhenQueriedByIdNameSalesman() {
		createCustomer();
		customerDao.addRecord(customer);
		
		results = customerDao.getQueryResults(customer.getVatNo(),customer.getName(), customer.getSalesman().getId());
		
		assertEquals("Firma", results.get(0).getName());
		assertEquals("John", results.get(0).getSalesman().getName());
		assertEquals("Krakow", results.get(0).getSalesman().getBranch().getName());
	}

	@Test
	public void shouldReturnExactRecordWhenQueriedWithMissingParam() {
		createCustomer();
		customerDao.addRecord(customer);
		
		results = customerDao.getQueryResults(customer.getVatNo(),customer.getName(), -1);
		
		assertEquals("Firma", results.get(0).getName());
		assertEquals("John", results.get(0).getSalesman().getName());
		assertEquals("Krakow", results.get(0).getSalesman().getBranch().getName());
	}
	
	@Test
	public void shouldReturnAnEmptyListWhenQuerirdByNonExistingRecord() {
		createCustomer();
		
		ArrayList<Customer> queryResult = customerDao.getQueryResults(customer.getVatNo());
		
		assertEquals(0, queryResult.size());
	}
	
	@Test
	public void shouldReturn1WhenUpdatingExistingRecord() {
		createCustomer();
		customerDao.addRecord(customer);
	
		customer.setName("ChangedName");
		
		assertEquals(1, customerDao.updateRecord(customer));
	}
	
	@Test
	public void shouldReturn0WhenUpdatingNonExistingRecord() {
		createCustomer();
		
		customer.setName("ChangedName");
		
		assertEquals(0, customerDao.updateRecord(customer));
	}
	
	@Test
	public void ShouldReturn1WhenRemovingExisitngRecordFromDB() {
		createCustomer();
		customerDao.addRecord(customer);
		
		assertEquals(1, customerDao.deleteRecord(customer));
	}
	
	@Test
	public void ShouldReturn0WhenRemovingNonExisitngRecordFromDB() {
		createCustomer();
		
		assertEquals(0, customerDao.deleteRecord(customer));
	}
	
	private void createCustomer() {
		
		customer = new Customer();
		salesman = new Salesman();
		branch = new Branch();
		branch.setName("Krakow");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));
		salesman.setName("John");
		salesman.setSurname("Doe");
		salesman.setBranch(branch);
		salesmanDao.addRecord(salesman);
		salesman.setId(salesmanDao.getIDSetByDB(salesman));
		customer.setVatNo("123-45-67-890");
		customer.setName("Firma");
		customer.setCity("Krakow");
		customer.setCountryCode("PL");
		customer.setSalesman(salesman);
		
		salesmanDao.addRecord(salesman);
	}
	
	@After
	public void tearDown() {
		if (customer != null) {
			final DBStatement dbStatement = new DBStatement();
			final String customerDeleteStmt = "DELETE FROM customers WHERE vatNo = ?";
			final String salesmanDeleteStmt = "DELETE FROM salesmen WHERE id = ?";
			final String branchDeleteStmt = "DELETE FROM branches WHERE name = ?";
			
			PreparedStatement prepStatement;
			try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(customerDeleteStmt);
				prepStatement.setString(1, customer.getVatNo());
				prepStatement.executeUpdate();
				prepStatement = connection.prepareStatement(salesmanDeleteStmt);
				prepStatement.setInt(1, salesman.getId());
				prepStatement.executeUpdate();
				prepStatement = connection.prepareStatement(branchDeleteStmt);
				prepStatement.setString(1, branch.getName());
				prepStatement.executeUpdate();
			} catch (SQLException e) {
				DialogUtils.errorDialog(e.getMessage());
			}
		}
	}

}
