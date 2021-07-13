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
import dbModel.DBStatement;
import dbModel.Salesman;
import dbUtils.BranchDAO;
import dbUtils.SalesmanDAO;
import utils.DialogUtils;

public class SalesmanDAOTest {
	
	Salesman salesman;
	Branch branch;
	SalesmanDAO salesmanDao;
	BranchDAO branchDao;
	List<Salesman> results;

	@Before
	public void setUp() throws Exception {
		salesmanDao = new SalesmanDAO();
		branchDao = new BranchDAO();
	}

	

	@Test
	public void shouldReturn1WhenAddingNewUniqueRecordToDB() {
		createNewSalesman();
		
		assertEquals(1, salesmanDao.addRecord(salesman));
	}
	
	@Test
	public void shouldReturn0WhenAddingDuplicatedRecordToDB() {
		createNewSalesman();
		
		salesmanDao.addRecord(salesman);
		
		assertEquals(0, salesmanDao.addRecord(salesman));
	}
	
	
	@Test
	public void shouldReturnExactRecordWhenQueriedByNameSurnameBranch() {
		createNewSalesman();
		salesmanDao.addRecord(salesman);
		
		results = salesmanDao.getQueryResults("John", "Doe", "Krakow");
		
		assertEquals("John", results.get(0).getName());
		assertEquals("Krakow", results.get(0).getBranch().getName());
	}
	
	@Test
	public void shouldReturnAnEmptyListWhenQuerirdByNonExistingRecord() {
		ArrayList<Salesman> queryResult = salesmanDao.getQueryResults("test", "test", "test");
		
		assertEquals(0, queryResult.size());
	}

	@Test
	public void shouldReturn1WhenUpdatingExistingRecord() {
		createNewSalesman();
		
		salesman.setId(salesmanDao.getIDSetByDB(salesman));
		
		salesman.setName("Joseph");
		
		assertEquals(1, salesmanDao.updateRecord(salesman));
	}
	
	@Test
	public void shouldReturn0WhenUpdatingNonExistingRecord() {
		createNewSalesman();
		
		assertEquals(0, salesmanDao.updateRecord(salesman));
	}
	
	@Test
	public void ShouldReturn1WhenRemovingExisitngSalesmanFromDB() {
		createNewSalesman();
		salesmanDao.addRecord(salesman);
		salesman.setId(salesmanDao.getIDSetByDB(salesman));
		
		assertEquals(1, salesmanDao.deleteRecord(salesman));
	}
	
	@Test
	public void shouldReturn0WhenRemovingNonExistingSalesmanFromDB() {
		createNewSalesman();
		
		assertEquals(0, salesmanDao.deleteRecord(salesman));
	}
	
	private void createNewSalesman() {
		salesman = new Salesman();
		branch = new Branch();
		branch.setName("Krakow");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));
		salesman.setName("John");
		salesman.setSurname("Doe");
		salesman.setBranch(branch);
	}
	
	@After
	public void tearDown() {
		if (salesman != null) {
			final DBStatement dbStatement = new DBStatement();
			final String salesmanDeleteStmt = "DELETE FROM salesmen WHERE name LIKE ? AND surname LIKE ?";
			final String branchDeleteStmt = "DELETE FROM branches WHERE name = ?";
			
			PreparedStatement prepStatement;
			try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(salesmanDeleteStmt);
				prepStatement.setString(1, salesman.getName());
				prepStatement.setString(2, salesman.getSurname());
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
