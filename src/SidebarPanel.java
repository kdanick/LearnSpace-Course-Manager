import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel {
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final int SIDE_WIDTH = 264;
    private final int SIDE_HEIGHT = 660;

    public SidebarPanel(JPanel contentPanel, CardLayout cardLayout, String username, String userRole) {
        this.contentPanel = contentPanel;
        this.cardLayout = cardLayout;

        setPreferredSize(new Dimension(SIDE_WIDTH, SIDE_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        // User Info Panel
        JPanel userInfo = new JPanel(new BorderLayout());
        userInfo.setBackground(Color.DARK_GRAY);
        userInfo.setPreferredSize(new Dimension(SIDE_WIDTH, 150));

        // Add username label
        JLabel usernameLabel = new JLabel(username, SwingConstants.CENTER);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        userInfo.add(usernameLabel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.DARK_GRAY);

        // Get button names based on user role
        String[] buttonNames = getButtonNames(userRole);

        // Create buttons and add them to the button panel
        for (String name : buttonNames) {
            JButton button = createButton(name);
            buttonPanel.add(button);
        }

        // Footer for the sidebar
        JLabel sideFooter = new JLabel("© 2025 LearnSpace. All Rights Reserved.", SwingConstants.CENTER);
        sideFooter.setForeground(Color.WHITE);
        sideFooter.setFont(new Font("Arial", Font.BOLD, 11));
        sideFooter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add panels to the sidebar
        add(userInfo, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
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
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(SIDE_WIDTH, 40));
        button.setPreferredSize(new Dimension(SIDE_WIDTH, 40));
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Hover effect for the button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 83, 85));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 63, 65));
            }
        });

        // Button click action
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Logout".equals(name)) {
                    showLogin();
                } else {
                    cardLayout.show(contentPanel, name);
                }
            }
        });

        return button; // Return the created button
    }

    // Method to show the initial panel based on user role
    private void showInitialPanel(String userRole) {
        if (userRole.equalsIgnoreCase("Admin")) {
            cardLayout.show(contentPanel, "Dashboard");
        } else if (userRole.equalsIgnoreCase("Lecturer")) {
            cardLayout.show(contentPanel, "Home");
        }
    }

    // Method to show the login screen
    private void showLogin() {
        contentPanel.removeAll();
        contentPanel.add(new Login(new App()));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}