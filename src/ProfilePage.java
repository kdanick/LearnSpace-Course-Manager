import javax.swing.*;
import java.awt.*;
import java.sql.*;
import DatabaseManager.Db_connect;

public class ProfilePage extends JPanel {
    private Integer user_id;
    private JLabel nameValue, emailValue, phoneValue, genderValue, roleValue;

    public ProfilePage(Integer user_id) {
        this.user_id = user_id;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        profilePanel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] userData = getUserData(user_id);
        nameValue = new JLabel(userData[0]);
        emailValue = new JLabel(userData[1]);
        phoneValue = new JLabel(userData[2]);
        genderValue = new JLabel(userData[3]);
        roleValue = new JLabel(userData[4]);

        // Labels
        addLabel(infoPanel, "Name:", nameValue);
        addLabel(infoPanel, "Email:", emailValue);
        addLabel(infoPanel, "Phone:", phoneValue);
        addLabel(infoPanel, "Gender:", genderValue);
        addLabel(infoPanel, "Role:", roleValue);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(new Color(255, 215, 0)); // Gold color
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.addActionListener(e -> openEditDialog());

        buttonPanel.add(editButton);

        // Assemble Profile Page
        profilePanel.add(infoPanel, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);
        add(profilePanel, BorderLayout.CENTER);
    }

    private void addLabel(JPanel panel, String text, JLabel valueLabel) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);
        panel.add(valueLabel);
    }

    private String[] getUserData(Integer user_id) {
        String[] data = {"Unknown", "Unknown", "Unknown", "Unknown", "Unknown"}; // Default values

        try (Connection conn = Db_connect.getConnection()) {
            if (conn != null) {
                String query = "SELECT name, email, phone_no, gender, role FROM Users WHERE user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    data[0] = rs.getString("name");
                    data[1] = rs.getString("email");
                    data[2] = rs.getString("phone_no");
                    data[3] = rs.getString("gender");
                    data[4] = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void openEditDialog() {
        JTextField nameField = new JTextField(nameValue.getText());
        JTextField emailField = new JTextField(emailValue.getText());
        JTextField phoneField = new JTextField(phoneValue.getText());
        JTextField genderField = new JTextField(genderValue.getText());
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Gender:", genderField,
                "New Password (Leave blank if unchanged):", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            updateUser(nameField.getText(), emailField.getText(), phoneField.getText(), genderField.getText(), new String(passwordField.getPassword()));
        }
    }

    private void updateUser(String name, String email, String phone, String gender, String password) {
        String query;
        if (password.isEmpty()) {
            query = "UPDATE Users SET name = ?, email = ?, phone_no = ?, gender = ? WHERE user_id = ?";
        } else {
            query = "UPDATE Users SET name = ?, email = ?, phone_no = ?, gender = ?, password_hash = ? WHERE user_id = ?";
        }

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, gender);
            if (!password.isEmpty()) {
                stmt.setString(5, password);
                stmt.setInt(6, user_id);
            } else {
                stmt.setInt(5, user_id);
            }
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            refreshProfileData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage());
        }
    }

    private void refreshProfileData() {
        String[] newUserData = getUserData(user_id);
        nameValue.setText(newUserData[0]);
        emailValue.setText(newUserData[1]);
        phoneValue.setText(newUserData[2]);
        genderValue.setText(newUserData[3]);
    }
}