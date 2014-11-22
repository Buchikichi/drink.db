package to.kit.drink.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * (仮).
 * @author H.Sasai
 */
public final class DBConnection {
	private static final String DB_URL = "jdbc:mysql://localhost/drink?characterEncoding=utf8";
	private static final String DB_USER = "drink";
	private static final String DB_PASS = "drunker";
	private static final DBConnection ME = new DBConnection();
	private Connection conn;

	private DBConnection() {
		try {
			this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static DBConnection getInstance() {
		return ME;
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// 無視
		}
	}
}
