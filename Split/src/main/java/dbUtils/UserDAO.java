package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbModel.Branch;
import dbModel.DBStatement;
import dbModel.User;
import utils.DialogUtils;

/*
 * Class responsible for data query and maintenance from 'Users' table of DB
 * */
public class UserDAO {

	protected DBStatement dbStatement;

	public UserDAO() {
		dbStatement = new DBStatement();
	}

	public ArrayList<User> getQueryResults(String name, String surname) {

		ArrayList<User> usersList = new ArrayList<User>();
		try {
			ResultSet resultSet = standardQuery(name, surname);

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getInt(1));
				user.setName(resultSet.getString(2));
				user.setSurname(resultSet.getString(3));
				user.setMachineID(resultSet.getString(4));
				user.setUserType(resultSet.getString(5));
				user.setBranchesAuthorizedForUser(getUserAuthorization(user));
				usersList.add(user);
			}

		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
		}
		return usersList;
	}

	/*
	 * returns '1' if record has been added to database or '0' if no change was
	 * applied into database, -1 if Exception occurred
	 */
	public int addUser(User user) {
		String userInsertStmt = "INSERT INTO users (name, surname, machineID, userType) VALUES(?,?,?,?)";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			if (!userExistsInDB(user)) {
				prepStatement = connection.prepareStatement(userInsertStmt);
				prepStatement.setString(1, user.getName());
				prepStatement.setString(2, user.getSurname());
				prepStatement.setString(3, user.getMachineID());
				prepStatement.setString(4, user.getUserType());
				return prepStatement.executeUpdate();
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}
	}

	/*
	 * returns number of updated records
	 */
	public int updateUser(User user) {
		String userUpdateStmt = "UPDATE users SET name = ?, surname = ?, machineID = ?, userType = ? WHERE id = ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(userUpdateStmt);
				prepStatement.setString(1, user.getName());
				prepStatement.setString(2, user.getSurname());
				prepStatement.setString(3, user.getMachineID());
				prepStatement.setString(4, user.getUserType());
				prepStatement.setInt(5, user.getId());
				return prepStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}
	}

	/*
	 * returns '1' if record was deleted from database or '0' if no change was
	 * applied in database
	 */
	public int deleteUser(User user) {
		String userDeleteStmt = "DELETE FROM users WHERE id = ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			if (userExistsInDB(user)) {
				prepStatement = connection.prepareStatement(userDeleteStmt);
				prepStatement.setInt(1, user.getId());
				return prepStatement.executeUpdate();
			} else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
			return 0;
		}
	}

	private ResultSet standardQuery(String name, String surname) throws SQLException {
		String userQuery = "SELECT id, name, surname, machineID, userType FROM users WHERE name LIKE ? AND surname LIKE ?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(userQuery);
		prepStatement.setString(1, name);
		prepStatement.setString(2, surname);
		return prepStatement.executeQuery();
	}

	private ArrayList<Branch> getUserAuthorization(User user) throws SQLException {
		ArrayList<Branch> resultList = new ArrayList<Branch>();
		String userAuthQuery = "SELECT ub.BranchID, b.name FROM users_branches AS ub JOIN branches AS b ON ub.BranchID = b.id WHERE ub.UserID = ?";
		PreparedStatement prepStatemet;
		Connection connection = dbStatement.getConnection();
		prepStatemet = connection.prepareStatement(userAuthQuery);
		prepStatemet.setInt(1, user.getId());
		ResultSet results = prepStatemet.executeQuery();
		while (results.next()) {
			Branch branch = new Branch();
			branch.setId(results.getInt(1));
			branch.setName(results.getString(2));
			resultList.add(branch);
		}
		return resultList;
	}

	public User userQueryByHostId(String machineID) {
		String userQuery = "SELECT id, name, surname, machineID, userType FROM users WHERE machineID LIKE ?";
		PreparedStatement prepStatement;
		ResultSet resultSet;
		User user = null;

		try (Connection connection = dbStatement.getConnection()) {
			prepStatement = connection.prepareStatement(userQuery);
			prepStatement.setString(1, machineID);
			resultSet = prepStatement.executeQuery();
			while (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt(1));
				user.setName(resultSet.getString(2));
				user.setSurname(resultSet.getString(3));
				user.setMachineID(resultSet.getString(4));
				user.setUserType(resultSet.getString(5));
				user.setBranchesAuthorizedForUser(getUserAuthorization(user));
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
		}
		return user;

	}

	public boolean userExistsInDB(User user) throws SQLException {
		ResultSet resultSet = standardQuery(user.getName(), user.getSurname());
		while (resultSet.next()) {
			if (user.getName().equals(resultSet.getString(2)) && user.getSurname().equals(resultSet.getString(3))) {
				return true;
			}
		}
		return false;
	}

	public int getIdSetByDB(User user) {
		String userIdQuery = "SELECT id FROM users WHERE name LIKE ? AND surname LIKE ? AND machineID LIKE ? AND userType LIKE ?";
		PreparedStatement prepStatement;
		int id = 0;
		try {
			Connection connection = dbStatement.getConnection();
			prepStatement = connection.prepareStatement(userIdQuery);
			prepStatement.setString(1, user.getName());
			prepStatement.setString(2, user.getSurname());
			prepStatement.setString(3, user.getMachineID());
			prepStatement.setString(4, user.getUserType());
			ResultSet result = prepStatement.executeQuery();
			while (result.next()) {
				id = result.getInt(1);
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
		}
		return id;

	}

}
