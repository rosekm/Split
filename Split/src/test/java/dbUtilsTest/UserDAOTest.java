package dbUtilsTest;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dbModel.Branch;
import dbModel.DBStatement;
import dbModel.User;
import dbUtils.BranchDAO;
import dbUtils.UserDAO;
import utils.DialogUtils;

public class UserDAOTest {

	User user;
	UserDAO userDao;
	List<User> results;

	@Before
	public void setUp() throws Exception {
		userDao = new UserDAO();
	}

	@Test
	public void shouldReturn1WhenAddingNewUniqueRecordtoDB() {
		createUser();

		assertEquals(1, userDao.addUser(user));
	}

	@Test
	public void shouldReturn0WhenAddingDuplicatedRecordtoDB() {
		createUser();

		userDao.addUser(user);

		assertEquals(0, userDao.addUser(user));
	}

	@Test
	public void shouldReturnExactRecordWhenQueriedByNameSurname() {
		createUser();
		userDao.addUser(user);

		results = userDao.getQueryResults("John", "Doe");

		assertEquals("John", results.get(0).getName());
		assertEquals("Doe", results.get(0).getSurname());
		assertEquals("STANDARD_USER", results.get(0).getUserType());
	}

	@Test
	public void shouldReturnAnEmptyListWhenQueriedByNonExistingRecord() {
		results = userDao.getQueryResults("John", "Doe");

		assertEquals(0, results.size());
	}

	@Test
	public void shouldReturn1WhenUpdatingExistingRecord() {
		createUser();
		userDao.addUser(user);
		user.setId(userDao.getIdSetByDB(user));
		
		user.setName("Jim");
		
		assertEquals(1, userDao.updateUser(user));
	}

	@Test
	public void shouldReturn0WhenUpdatingNonExistingRecord() {
		createUser();
		user.setId(0);
		
		assertEquals(0, userDao.updateUser(user));
	}

	@Test
	public void ShouldReturn1WhenRemovingExisitngRecordFromDB() {
		createUser();
		userDao.addUser(user);
		user.setId(userDao.getIdSetByDB(user));
		
		assertEquals(1, userDao.deleteUser(user));
	}

	@Test
	public void ShouldReturn0WhenRemovingNonExisitngRecordFromDB() {
		createUser();
		
		assertEquals(0, userDao.deleteUser(user));
	}

	private void createUser() {
		user = new User();

		user.setName("John");
		user.setSurname("Doe");
		user.setMachineID("Computer1");
		user.setUserType("STANDARD_USER");
	}

	@After
	public void tearDown() {
		if (user != null) {
			final DBStatement dbStatement = new DBStatement();
			final String userDeleteStmt = "DELETE FROM users WHERE name = ? AND surname = ?";
			;

			PreparedStatement prepStatement;
			try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(userDeleteStmt);
				prepStatement.setString(1, user.getName());
				prepStatement.setString(2, user.getSurname());
				prepStatement.executeUpdate();

			} catch (SQLException e) {
				DialogUtils.errorDialog(e.getMessage());
			}
		}
	}

}
