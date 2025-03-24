package DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db_connect {
    private static final String URL = "jdbc:postgresql://localhost:5432/learnspace";
    private static final String USER = "postgres";
    private static final String PASSWORD = "passcode";

    private Db_connect() {}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        Connection connection = getConnection();

        if (connection != null) {
            System.out.println("✅ Database connection successful!");
            try {
                connection.close();
                System.out.println("🔌 Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection.");
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Failed to connect to the database.");
        }
    }
}
