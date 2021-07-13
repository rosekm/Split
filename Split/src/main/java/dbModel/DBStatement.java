package dbModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBStatement {

	private Statement statement;
	private DBConnection connection;

	public DBStatement() {
		connection = new DBConnection();
	}

	private void createStatement() throws SQLException{
			statement = connection.getConnection().createStatement();
	}

	public Statement getStatement() throws SQLException{
		if (statement == null || statement.isClosed()) {
			createStatement();
		}
		return statement;
	}
	
	public Connection getConnection() throws SQLException {
		return connection.getConnection();
	}

	public void closeStatement() throws SQLException {
		if (statement != null) {
			statement.close();
			connection.closeDBConnection();
		}
	}

}
