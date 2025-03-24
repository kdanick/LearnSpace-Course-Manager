import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    // Database connection URL, username, and password
    private static final String URL = "jdbc:postgresql://localhost:5432/your_database"; // Replace with your database URL
    private static final String USER = "your_user"; // Replace with your database username
    private static final String PASSWORD = "your_password"; // Replace with your database password

    // Method to establish a connection to the database
    public static Connection connect() {
        try {
            // Attempt to create a connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
            return null; // Return null if the connection fails
        }
    }

    // Method to authenticate user credentials
    public static boolean authenticate(String username, String password) {
        // SQL query to select the password for the given username
        String sql = "SELECT password FROM users WHERE username = ?";

        // Use try-with-resources to ensure resources are closed automatically
        try (Connection conn = connect(); // Establish a connection
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement

            // Set the username parameter in the SQL query
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery(); // Execute the query

            // Check if a result is returned
            if (rs.next()) {
                // Retrieve the stored password from the result set
                String storedPassword = rs.getString("password");
                // Compare the stored password with the provided password
                return storedPassword.equals(password); // Replace with password hashing if needed
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
        return false; // Return false if authentication fails
    }
}