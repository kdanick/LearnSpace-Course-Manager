import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProfilePage extends JPanel {
    public ProfilePage(String userEmail) {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Main Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        profilePanel.setBackground(Color.WHITE);

        // Fetch User Data
        String[] userData = getUserData(userEmail);

        // Profile Picture Panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE);

        // Load Profile Picture
        ImageIcon profileImage = new ImageIcon(userData[4]); // Fetch image path from DB
        Image img = profileImage.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH); // Increased size
        JLabel profilePic = new JLabel(new ImageIcon(img));
        profilePic.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        profilePic.setPreferredSize(new Dimension(140, 140));

        imagePanel.add(profilePic);

        // Profile Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 2, 10, 10)); // Removed bio, so now 4 rows instead of 5
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

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel roleValue = new JLabel(userData[3]);

        // Adding elements to info panel
        infoPanel.add(nameLabel);
        infoPanel.add(nameValue);
        infoPanel.add(emailLabel);
        infoPanel.add(emailValue);
        infoPanel.add(phoneLabel);
        infoPanel.add(phoneValue);
        infoPanel.add(roleLabel);
        infoPanel.add(roleValue);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit Profile");
        editButton.setBackground(new Color(255, 215, 0)); // Gold color
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(editButton);

        //  Profile Page
        profilePanel.add(imagePanel, BorderLayout.NORTH);
        profilePanel.add(infoPanel, BorderLayout.CENTER);
        profilePanel.add(buttonPanel, BorderLayout.SOUTH);

        add(profilePanel, BorderLayout.CENTER);
    }

    //  Fetch User Data
    private String[] getUserData(String email) {
        String url = "jdbc:postgresql://localhost:5432/learnspaceDB";
        String user = "postgres";
        String password = "password";

        String[] data = {"Unknown", "Unknown", "Unknown", "Unknown", "default-profile.png"}; // Removed bio

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT name, email, phone, role, profile_picture FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                data[0] = rs.getString("name");
                data[1] = rs.getString("email");
                data[2] = rs.getString("phone");
                data[3] = rs.getString("role");
                data[4] = rs.getString("profile_picture") != null ? rs.getString("profile_picture") : "default-profile.png";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
