package DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Db_connect {
    private static final String URL = "jdbc:postgresql://localhost:5432/LearnspaceDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Mignonne2004!@";

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

    public static boolean authenticate(String name, String password_hash, String role) {
        // SQL query to select the password for the given username and role
        String sql = "SELECT password_hash FROM users WHERE name = ? AND role = ?";

        // Use try-with-resources to ensure resources are closed automatically
        try (Connection conn = getConnection(); // Establish a connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement

            // Set the username and role parameters in the SQL query
            pstmt.setString(1, name);
            pstmt.setString(2, role); // Set the role parameter

            ResultSet rs = pstmt.executeQuery(); // Execute the query

            // Check if a result is returned
            if (rs.next()) {
                // Retrieve the stored password from the result set
                String storedPassword = rs.getString("password_hash");
                // Compare the stored password with the provided password
                return storedPassword.equals(password_hash); // Replace with password hashing if needed
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
        return false; // Return false if authentication fails
    }
}