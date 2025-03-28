import DatabaseManager.Db_connect;
import Round.RoundedButton;
import baseClasses.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersPage extends JPanel {
    private JTable table;

    public UsersPage() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE);

        JLabel greetingLabel = new JLabel("User Management", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        greetingLabel.setForeground(Color.BLACK);

        topPanel.add(greetingLabel);
        add(topPanel, BorderLayout.NORTH);

        // Initialize the JTable
        initializeTable();

        // Fetch user data from the database
        refreshTable();

        // Button panel for user actions
        JPanel buttonPanel = new JPanel();
        RoundedButton addUserButton = new RoundedButton("Add User");
        RoundedButton editUserButton = new RoundedButton("Edit User");
        RoundedButton deleteUserButton = new RoundedButton("Delete User");

        addUserButton.addActionListener(e -> openUserDialog(null));
        editUserButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (Integer) table.getValueAt(selectedRow, 0);
                String username = (String) table.getValueAt(selectedRow, 1);
                String email = (String) table.getValueAt(selectedRow, 2);
                String password = (String) table.getValueAt(selectedRow, 3);
                String role = (String) table.getValueAt(selectedRow, 4);
                String phoneNumber = (String) table.getValueAt(selectedRow, 5);
                String gender = (String) table.getValueAt(selectedRow, 6);

                openUserDialog(new User(userId, username, email, password, role, phoneNumber, gender));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.");
            }
        });

        deleteUserButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (Integer) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteUser(userId);
                    refreshTable();
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

    private void initializeTable() {
        String[] columnNames = {"User ID", "Username", "Email", "Password", "Role", "Phone Number", "Gender"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void openUserDialog(User user) {
        JDialog dialog = new JDialog();
        dialog.setTitle(user == null ? "Add User" : "Edit User");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2));

        JTextField userIdField = new JTextField(String.valueOf(user != null ? user.getUser_id() : ""));
        JTextField usernameField = new JTextField(user != null ? user.getUsername() : "");
        JTextField emailField = new JTextField(user != null ? user.getEmail() : "");
        JTextField passwordField = new JTextField(user != null ? user.getPassword() : "");
        JTextField roleField = new JTextField(user != null ? user.getRole() : "");
        JTextField phoneField = new JTextField(user != null ? user.getPhoneNumber() : "");
        JTextField genderField = new JTextField(user != null ? user.getGender() : "");

        dialog.add(new JLabel("User id: "));
        dialog.add(userIdField);
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
        dialog.add(new JLabel("Gender:"));
        dialog.add(genderField);

        JButton saveButton = new JButton(user == null ? "Add" : "Save");
        saveButton.addActionListener(e -> {
            if (user == null) {
                addUser(Integer.parseInt((userIdField.getText().trim())),usernameField.getText().trim(), emailField.getText().trim(), passwordField.getText().trim(), roleField.getText().trim(), phoneField.getText().trim(), genderField.getText().trim());
            } else {
                updateUser(user.getUser_id(), usernameField.getText().trim(), emailField.getText().trim(), passwordField.getText().trim(), roleField.getText().trim(), phoneField.getText().trim(), genderField.getText().trim());
            }
            dialog.dispose();
            refreshTable();
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void addUser(int user_id, String name, String email, String password_hash, String role, String phone_no, String gender) {
        String sql = "INSERT INTO users (user_id, name, email, password_hash, role, phone_no, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Input validation
        if (name.isEmpty() || email.isEmpty() || password_hash.isEmpty() || role.isEmpty() || phone_no.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user_id); // Use setInt for integer user_id
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password_hash);
            pstmt.setString(5, role);
            pstmt.setString(6, phone_no);
            pstmt.setString(7, gender);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
        }
    }

    private void updateUser(int userId, String username, String email, String password, String role, String phoneNumber, String gender) {
        String sql = "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ?, phone_no = ?, gender = ? WHERE user_id = ?";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, gender);
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
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
        model.setRowCount(0);

        for (User user : users) {
            model.addRow(new Object[]{
                    user.getUser_id(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    user.getPhoneNumber(),
                    user.getGender()
            });
        }
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password_hash, role, phone_no, gender FROM users";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("phone_no"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
