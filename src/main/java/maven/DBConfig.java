package maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/authdb?useSSL=false";
	private static final String USER = "root";
	private static final String PASS = "Root@123";
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: MySQL driver not found.");
			System.exit(0);
		}
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {
			System.out.printf("Connection to DB failed.");
			System.exit(0);
		}
		return connection;
	}
}
