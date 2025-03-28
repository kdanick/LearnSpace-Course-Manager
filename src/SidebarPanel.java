import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {
    private final JPanel contentPanel; // Panel to display content based on selected button
    private final CardLayout cardLayout; // Layout manager for switching between different content panels
    private final int SIDE_WIDTH = 264; // Width of the sidebar
    private final int SIDE_HEIGHT = 660; // Height of the sidebar

    // Constructor for SidebarPanel
    public SidebarPanel(JPanel contentPanel, CardLayout cardLayout, String username, String userRole) {
        this.contentPanel = contentPanel; // Set the content panel
        this.cardLayout = cardLayout; // Set the card layout manager

        setPreferredSize(new Dimension(SIDE_WIDTH, SIDE_HEIGHT)); // Set sidebar dimensions
        setBackground(Color.DARK_GRAY); // Set background color
        setLayout(new BorderLayout()); // Set layout manager to BorderLayout

        // User Info Panel
        JPanel userInfo = new JPanel(new BorderLayout());
        userInfo.setBackground(Color.DARK_GRAY); // Background color of user info panel
        userInfo.setPreferredSize(new Dimension(SIDE_WIDTH, 150)); // Set size for user info panel

        // Add username label
        JLabel usernameLabel = new JLabel(username, SwingConstants.CENTER);
        usernameLabel.setForeground(Color.WHITE); // Set text color
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set font style

        userInfo.add(usernameLabel, BorderLayout.CENTER); // Add username label to user info panel

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Vertical box layout for buttons
        buttonPanel.setBackground(Color.DARK_GRAY); // Background color

        // Get button names based on user role
        String[] buttonNames = getButtonNames(userRole);

        // Create buttons and add them to the button panel
        for (String name : buttonNames) {
            JButton button = createButton(name); // Create a button for each name
            buttonPanel.add(button); // Add button to the button panel
        }

        // Footer for the sidebar
        JLabel sideFooter = new JLabel("© 2025 LearnSpace. All Rights Reserved.", SwingConstants.CENTER);
        sideFooter.setForeground(Color.WHITE); // Set text color
        sideFooter.setFont(new Font("Arial", Font.BOLD, 11)); // Set font style
        sideFooter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Add panels to the sidebar
        add(userInfo, BorderLayout.NORTH); // Add user info panel to the top
        add(buttonPanel, BorderLayout.CENTER); // Add button panel to the center
        add(sideFooter, BorderLayout.SOUTH); // Add footer to the bottom

        // Show the initial panel based on user role
        showInitialPanel(userRole);
    }

    // Method to get button names based on user role
    private String[] getButtonNames(String userRole) {
        if (userRole.equalsIgnoreCase("Admin")) {
            return new String[] {"Dashboard", "Students", "Lecturers", "Courses", "Users", "Profile", "Logout"};
        } else if (userRole.equalsIgnoreCase("Lecturer")) {
            return new String[] {"Home", "Enrollments", "My Courses", "Grades", "Profile", "Logout"};
        } else {
            return new String[] {"LoginPage"}; // Default button for other roles
        }
    }

    // Method to create a button with specified name
    private JButton createButton(String name) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button alignment
        button.setMaximumSize(new Dimension(SIDE_WIDTH, 40)); // Set maximum size
        button.setPreferredSize(new Dimension(SIDE_WIDTH, 40)); // Set preferred size
        button.setBackground(new Color(60, 63, 65)); // Set button background color
        button.setForeground(Color.WHITE); // Set button text color
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font style
        button.setFocusPainted(false); // Remove focus painting
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Hover effect for the button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 83, 85));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 63, 65)); // Reset background color when not hovered
            }
        });

        // Button click action
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Logout".equals(name)) {
                    showLogin(); // Show login screen if Logout button is clicked
                } else {
                    cardLayout.show(contentPanel, name); // Show the corresponding content panel
                }
            }
        });

        return button; // Return the created button
    }

    // Method to show the initial panel based on user role
    private void showInitialPanel(String userRole) {
        if (userRole.equalsIgnoreCase("Admin")) {
            cardLayout.show(contentPanel, "Dashboard"); // Show Dashboard for Admin
        } else if (userRole.equalsIgnoreCase("Lecturer")) {
            cardLayout.show(contentPanel, "Home"); // Show Home for Lecturer
        }
    }

    // Method to show the login screen
    private void showLogin() {
        contentPanel.removeAll(); // Clear the content panel
        contentPanel.add(new Login(new App())); // Add the Login panel
        contentPanel.revalidate(); // Refresh the content panel
        contentPanel.repaint(); // Repaint the content panel
    }
}