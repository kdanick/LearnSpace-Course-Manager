package DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Db_connect {
    private static final String URL = "jdbc:postgresql://localhost:5432/Your-database-name"; // Use your Actual postgres database name
    private static final String USER = "your-username"; // Your actual username
    private static final String PASSWORD = "Your-database-password"; // Your actual password

    private Db_connect() {
    }

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

    public static Integer authenticate(String name, String password_hash, String role) {
        String sql = "SELECT user_id, password_hash FROM users WHERE name = ? AND role = ?";

        // Use try-with-resources to ensure resources are closed automatically
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the username and role parameters in the SQL query
            pstmt.setString(1, name);
            pstmt.setString(2, role);

            ResultSet rs = pstmt.executeQuery();

            // Check if a result is returned
            if (rs.next()) {
                // Retrieve the stored password from the result set
                String storedPassword = rs.getString("password_hash");
                Integer user_id = rs.getInt("user_Id");

                if (storedPassword.equals(password_hash)){
                    return user_id;
                }
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
        return null;
    }
}