import javax.swing.*;
import java.awt.*;
import java.sql.*;
import DatabaseManager.Db_connect;

public class ProfilePage extends JPanel {
    public ProfilePage(Integer user_id) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Main Profile Panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        profilePanel.setBackground(Color.WHITE);

        // Fetch User Data
        String[] userData = getUserData(user_id);

        // Profile Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel nameValue = new JLabel(userData[0]);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel emailValue = new JLabel(userData[1]);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel phoneValue = new JLabel(userData[2]);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel genderValue = new JLabel(userData[3]);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel roleValue = new JLabel(userData[4]);

        // Add fields to info panel
        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(emailLabel);
        infoPanel.add(emailValue);
        infoPanel.add(phoneLabel);
        infoPanel.add(phoneValue);
        infoPanel.add(genderLabel);
        infoPanel.add(genderValue);
        infoPanel.add(roleLabel);
        infoPanel.add(roleValue);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(new Color(255, 215, 0)); // Gold color
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(editButton);

        // Assemble Profile Page
        profilePanel.add(infoPanel, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);

        add(profilePanel, BorderLayout.CENTER);
    }

    // Fetch User Data from Database
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
}
