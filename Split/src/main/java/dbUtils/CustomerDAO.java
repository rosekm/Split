package dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dbModel.Branch;
import dbModel.Customer;
import dbModel.DBStatement;
import dbModel.Salesman;
import utils.DialogUtils;

/*
 * Class responsible for data query and maintenance from 'Customers' table of DB
 * */
public class CustomerDAO implements CommonDAO<Customer, String> {

	private DBStatement dbStatement;

	public CustomerDAO() {
		dbStatement = new DBStatement();
	}

	public ArrayList<Customer> getQueryResults(String vatNo, String name, Integer salesmanID) {

		ArrayList<Customer> customerList = new ArrayList<>();
		try {
			ResultSet resultSet = executeQuery(vatNo, name, salesmanID);
			while (resultSet.next()) {
				Branch branch = new Branch();
				Salesman salesman = new Salesman();
				Customer customer = new Customer();
				customer.setVatNo(resultSet.getString(1));
				customer.setName(resultSet.getString(2));
				customer.setAddress(resultSet.getString(3));
				customer.setPostalCode(resultSet.getString(4));
				customer.setCity(resultSet.getString(5));
				customer.setCountryCode(resultSet.getString(6));
				salesman.setId(resultSet.getInt(7));
				salesman.setName(resultSet.getString(8));
				salesman.setSurname(resultSet.getString(9));
				branch.setId(resultSet.getInt(10));
				branch.setName(resultSet.getString(11));
				salesman.setBranch(branch);
				customer.setSalesman(salesman);
				customerList.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return customerList;
	}

	public ArrayList<Customer> getQueryResults(String vatNo) {

		ArrayList<Customer> customerList = new ArrayList<>();
		try {
			ResultSet resultSet = executeQuery(vatNo);
			while (resultSet.next()) {
				Branch branch = new Branch();
				Salesman salesman = new Salesman();
				Customer customer = new Customer();
				customer.setVatNo(resultSet.getString(1));
				customer.setName(resultSet.getString(2));
				customer.setAddress(resultSet.getString(3));
				customer.setPostalCode(resultSet.getString(4));
				customer.setCity(resultSet.getString(5));
				customer.setCountryCode(resultSet.getString(6));
				salesman.setId(resultSet.getInt(7));
				salesman.setName(resultSet.getString(8));
				salesman.setSurname(resultSet.getString(9));
				branch.setId(resultSet.getInt(10));
				branch.setName(resultSet.getString(11));
				salesman.setBranch(branch);
				customer.setSalesman(salesman);
				customerList.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return customerList;
	}

	/*
	 * returns '1' if record has been added to database or '0' if no change was
	 * applied into database, -1 if Exception occurred
	 */
	@Override
	public int addRecord(Customer customer) {
		String customerInsertStmt = "INSERT INTO customers (vatNo, name, address, postalCode, city, countryCode, salesman_id) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement prepStatement;

		try (Connection connection = dbStatement.getConnection()) {
			if (!customerExistsInDB(customer)) {
				prepStatement = connection.prepareStatement(customerInsertStmt);
				prepStatement.setString(1, customer.getVatNo().trim()); // mandatory field
				prepStatement.setString(2, customer.getName().trim()); // mandatory field
				prepStatement.setString(3, customer.getAddress());
				prepStatement.setString(4, customer.getPostalCode());
				prepStatement.setString(5, customer.getCity());
				prepStatement.setString(6, customer.getCountryCode()); // mandatory field
				prepStatement.setInt(7, customer.getSalesman().getId()); // mandatory field
				return prepStatement.executeUpdate();
			} else {
				return 0;
			}
		} catch (SQLException e) {
			// DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	/*
	 * returns number of updated records
	 */
	@Override
	public int updateRecord(Customer customer) {
		String customerUpdateStmt = "UPDATE customers SET name = ?, address = ?, postalCode = ?, city=?, countryCode = ?, salesman_id = ? WHERE vatNo = ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			if (customerExistsInDB(customer)) {
				prepStatement = connection.prepareStatement(customerUpdateStmt);
				prepStatement.setString(1, customer.getName().trim());
				prepStatement.setString(2,
						customer.getAddress() == null ? "NULL" : customer.getAddress().trim());
				prepStatement.setString(3,
						customer.getPostalCode() == null ? "NULL" : customer.getPostalCode().trim());
				prepStatement.setString(4, customer.getCity() == null ? "NULL" : customer.getCity().trim());
				prepStatement.setString(5, customer.getCountryCode());
				prepStatement.setInt(6, customer.getSalesman().getId());
				prepStatement.setString(7, customer.getVatNo().trim());
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
	 * returns '1' if record was deleted from database or '0' if no change was
	 * applied in database
	 */
	@Override
	public int deleteRecord(Customer customer) {
		String customerDeleteStmt = "DELETE FROM customers WHERE vatNo LIKE ?";
		PreparedStatement prepStatement;
		try (Connection connection = dbStatement.getConnection()) {
			if (customerExistsInDB(customer)) {
				prepStatement = connection.prepareStatement(customerDeleteStmt);
				prepStatement.setString(1, customer.getVatNo().trim());
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

	public boolean customerExistsInDB(Customer customer) throws SQLException {
		ResultSet resultSet = executeQuery(customer.getVatNo().trim());
		while (resultSet.next()) {
			if (customer.getVatNo().equals(resultSet.getString(1))) {
				return true;
			}
		}
		return false;
	}

	private ResultSet executeQuery(String vatNo, String name, Integer salesmanID) throws SQLException {
		String customerQuery = "SELECT c.vatNo, c.name, c.address, c.postalCode, c.city, c.countryCode, s.id, s.name, s.surname, b.id, b.name FROM customers AS c "
				+ "JOIN salesmen AS s ON c.salesman_id = s.id " + "JOIN branches AS b ON s.branch_id = b.id "
				+ "WHERE c.vatNo LIKE ? AND c.name LIKE ? AND CAST(c.salesman_id AS CHAR) LIKE ?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(customerQuery);
		prepStatement.setString(1, vatNo == null ? "%" : vatNo.trim());
		prepStatement.setString(2, name == null ? "%" : name.trim());
		prepStatement.setObject(3, salesmanID == -1 ? "%" : salesmanID);
		ResultSet resultSet = prepStatement.executeQuery();
		return resultSet;

	}

	private ResultSet executeQuery(String vatNo) throws SQLException {
		String customerQuery = "SELECT c.vatNo, c.name, c.address, c.postalCode, c.city, c.countryCode, s.id, s.name, s.surname, b.id, b.name FROM customers AS c "
				+ "JOIN salesmen AS s ON c.salesman_id = s.id " + "JOIN branches AS b ON s.branch_id = b.id "
				+ "WHERE c.vatNo LIKE ?";
		PreparedStatement prepStatement;
		Connection connection = dbStatement.getConnection();
		prepStatement = connection.prepareStatement(customerQuery);
		prepStatement.setString(1, vatNo.trim());
		ResultSet resultSet = prepStatement.executeQuery();
		return resultSet;
	}

	@Override
	public String getIDSetByDB(Customer customer) {
		StringBuilder customerIdQuery = new StringBuilder(
				"SELECT vatNo FROM customers WHERE name LIKE '" + customer.getName().trim() + "'");
		String id = null;
		try {
			Connection connection = dbStatement.getConnection();
			Statement statement = connection.createStatement();
			if (customer.getAddress() == null) {
				customerIdQuery.append(" AND address IS NULL");
			} else {
				customerIdQuery.append(" AND address LIKE  '" + customer.getAddress().trim() + "'");
			}
			if (customer.getPostalCode() == null) {
				customerIdQuery.append(" AND postalCode IS NULL");
			} else {
				customerIdQuery.append(" AND PostalCode LIKE '" + customer.getPostalCode().trim() + "'");
			}
			if (customer.getCity() == null) {
				customerIdQuery.append(" AND city IS NULL");
			} else {
				customerIdQuery.append(" AND city LIKE '" + customer.getCity().trim() + "'");
			}
			customerIdQuery.append(" AND countryCode LIKE '" + customer.getCountryCode() + "'");
			ResultSet result = statement.executeQuery(customerIdQuery.toString());
			while (result.next()) {
				id = result.getString(1);
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DialogUtils.errorDialog(e.getMessage());
		}
		return id;

	}
}
