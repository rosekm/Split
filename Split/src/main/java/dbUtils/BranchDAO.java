package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dbModel.Branch;
import dbModel.DBStatement;
import utils.DialogUtils;

/*
 * Class responsible for data query and maintenance from 'Branches' table of DB
 * */
public class BranchDAO implements CommonDAO<Branch, Integer> {

	protected DBStatement dbStatement;

	public BranchDAO() {
		dbStatement = new DBStatement();
	}

	public ArrayList<Branch> getQueryResults(String name) {

		ArrayList<Branch> branchList = new ArrayList<>();
		try {
			ResultSet resultSet = standardQuery(name);
			while (resultSet.next()) {
				Branch branch = new Branch();
				branch.setId(resultSet.getInt(1));
				branch.setName(resultSet.getString(2));
				branchList.add(branch);
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
		}
		return branchList;
	}

	/*
	 * returns '1' if record has been added to database or '0' if no change was
	 * applied into database, -1 if Exception occurred
	 */
	@Override
	public int addRecord(Branch branch) {
		String branchInsertStmt = "INSERT INTO branches (name) VALUES(?)";
		PreparedStatement prepStatement;

		try (Connection connection = dbStatement.getConnection()) {
			if (!branchExistsInDB(branch)) {
				prepStatement = connection.prepareStatement(branchInsertStmt);
				prepStatement.setString(1, branch.getName().trim());
				return prepStatement.executeUpdate();
			} else {
				return 0;
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}

	}

	/*
	 * returns number of updated records
	 */
	@Override
	public int updateRecord(Branch branch) {
		String branchUpdateStmt = "UPDATE branches SET name = ? WHERE id = ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			prepStatement = connection.prepareStatement(branchUpdateStmt);
			prepStatement.setString(1, branch.getName().trim());
			prepStatement.setInt(2, branch.getId());
			return prepStatement.executeUpdate();
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}
	}

	/*
	 * returns '1' if record was deleted from database or '0' if no change was
	 * applied in database
	 */
	@Override
	public int deleteRecord(Branch branch) {
		String branchDeleteStmt = "DELETE FROM branches WHERE id = ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			if(branchExistsInDB(branch)) {
			prepStatement = connection.prepareStatement(branchDeleteStmt);
			prepStatement.setInt(1, branch.getId());
			return prepStatement.executeUpdate();
			}else {
				return 0;
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}
	}

	public ArrayList<Branch> queryForAll() {
		String branchesAllQuery = "SELECT id, name FROM branches";
		ArrayList<Branch> branchList = new ArrayList<>();

		try (Connection connection = dbStatement.getConnection()) {
			Statement statement = dbStatement.getStatement();
			ResultSet branches = statement.executeQuery(branchesAllQuery);
			while (branches.next()) {
				Branch b = new Branch();
				b.setId(branches.getInt(1));
				b.setName(branches.getString(2));
				branchList.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return branchList;

	}

	private ResultSet standardQuery(String name) throws SQLException {
		String branchQuery = "SELECT id, name FROM branches WHERE name LIKE ?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(branchQuery);
		prepStatement.setString(1, name == null ? "%" : name.trim());
		return prepStatement.executeQuery();
	}

	public ResultSet standardQuery(Integer id) throws SQLException {
		String branchQuery = "SELECT id, name FROM branches WHERE id =?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(branchQuery);
		prepStatement.setInt(1, id);
		return prepStatement.executeQuery();
	}

	public boolean branchExistsInDB(Branch branch) throws SQLException {
		ResultSet resultSet = standardQuery(branch.getName());
		while (resultSet.next()) {
			if (branch.getName().equals(resultSet.getString(2))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer getIDSetByDB(Branch branch) {
		String branchIdQuery = "SELECT id FROM branches WHERE name LIKE ?";
		PreparedStatement prepStatement;
		int id = 0;
		try {
			Connection connection = dbStatement.getConnection();
			prepStatement = connection.prepareStatement(branchIdQuery);
			prepStatement.setString(1, branch.getName().trim());
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
