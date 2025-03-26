import DatabaseManager.Db_connect;
import Round.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersPage extends JPanel {
    private JTable table; // Make table accessible for refreshing

    public UsersPage() {
        setBackground(Color.WHITE); // White background
        setLayout(new BorderLayout());

        // Top Panel with Greetings
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE); // White background

        JLabel greetingLabel = new JLabel("User Management", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        greetingLabel.setForeground(Color.BLACK); // Black text

        topPanel.add(greetingLabel);
        add(topPanel, BorderLayout.NORTH);

        // Fetch user data from the database
        List<User> users = getAllUsers();

        // Prepare table data
        String[] columnNames = {"User ID", "Username", "Email", "Password", "Role", "Phone Number"};
        Object[][] userData = new Object[users.size()][columnNames.length];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userData[i][0] = user.getUser_id();
            userData[i][1] = user.getUsername();
            userData[i][2] = user.getEmail();
            userData[i][3] = user.getPassword();
            userData[i][4] = user.getRole();
            userData[i][5] = user.getPhoneNumber();
        }

        table = new JTable(new DefaultTableModel(userData, columnNames));
        table.setBackground(Color.WHITE); // White background
        table.setForeground(Color.BLACK); // Black text
        table.setGridColor(Color.BLACK); // Black grid lines
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for user actions
        JPanel buttonPanel = new JPanel();
        RoundedButton addUserButton = new RoundedButton("Add User");
        RoundedButton editUserButton = new RoundedButton("Edit User");
        RoundedButton deleteUserButton = new RoundedButton("Delete User");

        // Add action listeners for buttons
        addUserButton.addActionListener(e -> openUserDialog(null)); // For adding a user

        editUserButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) { // Ensure a user is selected
                String userId = (String) table.getValueAt(selectedRow, 0);
                String username = (String) table.getValueAt(selectedRow, 1);
                String email = (String) table.getValueAt(selectedRow, 2);
                String password = (String) table.getValueAt(selectedRow, 3);
                String role = (String) table.getValueAt(selectedRow, 4);
                String phoneNumber = (String) table.getValueAt(selectedRow, 5);

                // Open dialog to edit the selected user
                openUserDialog(new User(userId, username, email, password, role, phoneNumber));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            }
        });

        deleteUserButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String userId = (String) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteUser(userId); // Call method to delete user
                    refreshTable(); // Refresh table after deletion
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            }
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openUserDialog(User user) {
        JDialog dialog = new JDialog();
        dialog.setTitle(user == null ? "Add User" : "Edit User");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField user_idField = new JTextField(user != null ? user.getUser_id() : "");
        JTextField usernameField = new JTextField(user != null ? user.getUsername() : "");
        JTextField emailField = new JTextField(user != null ? user.getEmail() : "");
        JTextField passwordField = new JTextField(user != null ? user.getPassword() : "");
        JTextField roleField = new JTextField(user != null ? user.getRole() : "");
        JTextField phoneField = new JTextField(user != null ? user.getPhoneNumber() : "");

        dialog.add(new JLabel("User ID:"));
        dialog.add(user_idField);
        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleField);
        dialog.add(new JLabel("Phone Number:"));
        dialog.add(phoneField);

        JButton saveButton = new JButton(user == null ? "Add" : "Save");
        saveButton.addActionListener(e -> {
            if (user == null) {
                addUser(user_idField.getText(), usernameField.getText(), emailField.getText(), passwordField.getText(), roleField.getText(), phoneField.getText());
            } else {
                updateUser(user.getUser_id(), usernameField.getText(), emailField.getText(), passwordField.getText(), roleField.getText(), phoneField.getText());
            }
            dialog.dispose(); // Close the dialog
            refreshTable(); // Refresh the table after adding/editing
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void addUser(String user_id, String name, String email, String password_hash, String role, String phone_no) {
        String sql = "INSERT INTO users (user_id, name, email, password_hash, role, phone_no) VALUES (?, ?, ?, ?, ?, ?)";

        // Input validation
        if (name.isEmpty() || email.isEmpty() || password_hash.isEmpty() || role.isEmpty() || phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password_hash);
            pstmt.setString(5, role);
            pstmt.setString(6, phone_no);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
        }
    }

    private void updateUser(String userId, String username, String email, String password, String role, String phoneNumber) {
        String sql = "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ?, phone_no = ? WHERE user_id = ?";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, userId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
        }
    }

    private void refreshTable() {
        List<User> users = getAllUsers();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        for (User user : users) {
            model.addRow(new Object[]{
                    user.getUser_id(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    user.getPhoneNumber()
            });
        }
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password_hash, role, phone_no FROM users";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String user_id = rs.getString("user_id");
                String name = rs.getString("name"); // Corrected column name
                String email = rs.getString("email");
                String password_hash = rs.getString("password_hash"); // Corrected column name
                String role = rs.getString("role");
                String phone_no = rs.getString("phone_no"); // Corrected column name
                users.add(new User(user_id, name, email, password_hash, role, phone_no));
            }
            System.out.println("Fetched users: " + users.size()); // Debugging line
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Inner User class
    private class User {
        private String user_id;
        private String name;
        private String email;
        private String password_hash;
        private String role;
        private String phone_no;

        public User(String user_id, String name, String email, String password_hash, String role, String phone_no) {
            this.user_id = user_id;
            this.name = name;
            this.email = email;
            this.password_hash = password_hash;
            this.role = role;
            this.phone_no = phone_no;
        }

        // Getters
        public String getUser_id() { return user_id; }
        public String getUsername() { return name; }
        public String getEmail() { return email; }
        public String getPassword() { return password_hash; }
        public String getRole() { return role; }
        public String getPhoneNumber() { return phone_no; }
    }
}