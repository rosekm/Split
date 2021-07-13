package dbUtilsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dbModel.Branch;
import dbModel.DBStatement;
import dbUtils.BranchDAO;
import utils.DialogUtils;

public class BranchDAOTest {

	Branch branch;
	BranchDAO branchDao;
	List<Branch> results;

	@Before
	public void setUp() throws Exception {
		branchDao = new BranchDAO();
	}

	@Test
	public void shouldReturn1whenAddingUniqueRecordToDB() {
		createNewBranch("City");

		assertEquals(1, branchDao.addRecord(branch));
	}
	
	@Test
	public void shouldReturn0whenAddingDuplicateRecordToDB() {
		createNewBranch("City");
		
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));

		assertEquals(0, branchDao.addRecord(branch));
	}
	
	@Test
	public void shouldReturnExactBranchNameAsAddedWhenQueriedByName() {
		createNewBranch("City");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));

		results = branchDao.getQueryResults("City");

		assertEquals(results.get(0).getName(), "City");
	}

	@Test
	public void shouldReturnAnEmptyListWhenQuerirdByNonExistingName() {
		assertEquals(0, branchDao.getQueryResults("City").size());
	}

	@Test
	public void shouldReturnCorrectBranchIDWhenQueriedByBranchName() {
		createNewBranch("City");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));
		
		assertNotEquals(new Integer(0), branchDao.getIDSetByDB(branch));
	}
	
	@Test
	public void shouldReturn0WhenQueriedIDByNonExistingName() {
		createNewBranch("City");
		
		assertEquals(new Integer(0), branchDao.getIDSetByDB(branch));
	}

	@Test
	public void shouldReturn1WhenUpdateExistingRecordWithNewData() {
		createNewBranch("City");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));
		branch.setName("City3");

		assertEquals(1, branchDao.updateRecord(branch));
	}

	@Test
	public void shouldReturn0WhenUpdatingNonExistingRecord() {
		createNewBranch("City");

		assertEquals(0, branchDao.updateRecord(branch));
	}

	@Test
	public void shouldReturn1WhenRemovingExisitngBranchFromDB() {
		createNewBranch("City");
		branchDao.addRecord(branch);
		branch.setId(branchDao.getIDSetByDB(branch));

		assertEquals(1, branchDao.deleteRecord(branch));
	}

	@Test
	public void shouldReturn0WhenRemovingNonExistingBranchFromDB() {
		createNewBranch("City");

		assertEquals(0, branchDao.deleteRecord(branch));
	}

	private void createNewBranch(String branchName) {
		branch = new Branch();
		branch.setName(branchName);
	}

	@After
	public void tearDown() {
		if (branch != null) {
			final DBStatement dbStatement = new DBStatement();
			final String branchDeleteStmt = "DELETE FROM branches WHERE name = ?";
			PreparedStatement prepStatement;
			try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(branchDeleteStmt);
				prepStatement.setString(1, branch.getName());
				prepStatement.executeUpdate();
			} catch (SQLException e) {
				DialogUtils.errorDialog(e.getMessage());
			}
		}
	}

}
