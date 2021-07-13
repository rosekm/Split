package dbModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private final String DATABASE_URL = "jdbc:mysql://localhost/split? user=root&password=";
	private Connection connection;

	public void connectToDB() throws SQLException {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "");
	}

	public void closeDBConnection() throws SQLException {
		if (connection != null) {
				connection.close();
		}
	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connectToDB();
		}
		return connection;
	}

}
