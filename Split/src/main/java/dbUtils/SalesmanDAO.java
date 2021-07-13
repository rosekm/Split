package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dbModel.Branch;
import dbModel.DBStatement;
import dbModel.Salesman;
import utils.DialogUtils;

/*
 * Class responsible for data query and maintenance from 'Salesman' table of DB
 * */
public class SalesmanDAO implements CommonDAO<Salesman, Integer> {

	private DBStatement dbStatement;

	public SalesmanDAO() {
		dbStatement = new DBStatement();
	}

	public ArrayList<Salesman> getQueryResults(String name, String surname, String branchName) {
		ArrayList<Salesman> salesmenList = new ArrayList<>();
		
		try {
			ResultSet resultSet = standardQuery(name, surname, branchName);
			while (resultSet.next()) {
				Salesman salesman = new Salesman();
				Branch branch = new Branch();
				salesman.setId(resultSet.getInt(1));
				salesman.setName(resultSet.getString(2));
				salesman.setSurname(resultSet.getString(3));
				branch.setId(resultSet.getInt(4));
				branch.setName(resultSet.getString(5));
				salesman.setBranch(branch);
				salesmenList.add(salesman);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return salesmenList;
	}

	/*
	 * returns '1' if record has been added to database or '0' if no change was
	 * applied into database, -1 if Exception occurred
	 */
	@Override
	public int addRecord(Salesman salesman) {
		String salesmanInsertStmt = "INSERT INTO salesmen (name, surname, branch_ID) VALUES(?,?,?)";
		PreparedStatement prepStatement;
		
		try (Connection connection = dbStatement.getConnection()) {
			if (!salesmanExistsInDB(salesman)) {
				prepStatement = connection.prepareStatement(salesmanInsertStmt);
				prepStatement.setString(1, salesman.getName().trim());
				prepStatement.setString(2, salesman.getSurname().trim());
				prepStatement.setInt(3, salesman.getBranch().getId());
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
	@Override
	public int updateRecord(Salesman salesman) {
		String salesmanUpdateStmt = "UPDATE salesmen SET name = ?, surname = ?, branch_id = ? WHERE id = ?";
		PreparedStatement prepStatement;

		try (Connection connection = dbStatement.getConnection()) {
				prepStatement = connection.prepareStatement(salesmanUpdateStmt);
				prepStatement.setString(1, salesman.getName().trim());
				prepStatement.setString(2, salesman.getSurname().trim());
				prepStatement.setInt(3, salesman.getBranch().getId());
				prepStatement.setInt(4, salesman.getId());
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
	public int deleteRecord(Salesman salesman) {
		String salesmanDeleteStmt = "DELETE FROM salesmen WHERE id = ?";
		PreparedStatement prepStatement;
		
		try (Connection connection = dbStatement.getConnection()) {
			if(salesmanExistsInDB(salesman)) {
			prepStatement = connection.prepareStatement(salesmanDeleteStmt);
			prepStatement.setInt(1, salesman.getId());
			return prepStatement.executeUpdate();
			}else {
				return 0;
			}
		} catch (SQLException e) {
			DialogUtils.errorDialog(e.getMessage());
			return -1;
		}
	}

	public ArrayList<Salesman> queryForAll() {
		String salesmanAllQuery = "SELECT s.id, s.name, s.surname, b.id, b.name FROM salesmen s join branches b on s.branch_id = b.id";
		ArrayList<Salesman> salesmenList = new ArrayList<>();

		try (Connection connection = dbStatement.getConnection()) {
			Statement statement = dbStatement.getStatement();
			ResultSet salesmen = statement.executeQuery(salesmanAllQuery);
			while (salesmen.next()) {
				Salesman s = new Salesman();
				Branch b = new Branch();
				s.setId(salesmen.getInt(1));
				s.setName(salesmen.getString(2));
				s.setSurname(salesmen.getString(3));
				b.setId(salesmen.getInt(4));
				b.setName(salesmen.getString(5));
				s.setBranch(b);
				salesmenList.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return salesmenList;

	}

	public ResultSet standardQuery(String name, String surname, String branchName) throws SQLException {
		String salesmanQuery = "SELECT s.id, s.name, s.surname, b.id, b.name FROM salesmen AS s JOIN branches AS b ON s.branch_id = b.id WHERE s.name LIKE ? AND s.surname LIKE ? AND b.name LIKE ?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(salesmanQuery);
		prepStatement.setString(1, name == null ? "%" : name.trim());
		prepStatement.setString(2, surname == null ? "%" : surname.trim());
		prepStatement.setString(3, branchName == null ? "%" : branchName.trim());
		return prepStatement.executeQuery();
	}

	public ResultSet standardQuery(Integer id) throws SQLException {
		String salesmanQuery = "SELECT s.id, s.name, s.surname, b.id, b.name FROM salesmen s join branches b on s.branch_id = b.id WHERE s.id =?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(salesmanQuery);
		prepStatement.setInt(1, id);
		return prepStatement.executeQuery();
	}

	public boolean salesmanExistsInDB(Salesman salesman) throws SQLException {
		ResultSet resultSet = standardQuery(salesman.getName(), salesman.getSurname(), salesman.getBranch().getName());
		while (resultSet.next()) {
			if (salesman.getName().equals(resultSet.getString(2)) && salesman.getSurname().equals(resultSet.getString(3)) && salesman.getBranch().getName().equals(resultSet.getString(5))){
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer getIDSetByDB(Salesman salesman) {
		String salesmanIdQuery = "SELECT id FROM salesmen WHERE name = ? AND surname = ?";
		PreparedStatement prepStatement;
		int id = 0;
		
		try {
			Connection connection = dbStatement.getConnection();
			prepStatement = connection.prepareStatement(salesmanIdQuery);
			prepStatement.setString(1, salesman.getName().trim());
			prepStatement.setString(2, salesman.getSurname().trim());
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
