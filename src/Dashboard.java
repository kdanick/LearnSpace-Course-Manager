import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel sidebar;
    private boolean isSidebarVisible = true;

    public Dashboard() {
        setTitle("Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar Panel
        sidebar = new JPanel();
        sidebar.setBackground(new Color(40, 40, 40));
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Sidebar Items with Dynamic Dropdowns
        String[][] menuItems = {
                {"Home"},
                {"Students"},
                {"Courses"},
                {"Enrollment"},
                {"Profile", "Edit Profile", "View Stats"},
                {"Settings", "Account Settings", "Privacy Settings"},
                {"Logout"}
        };

        // Loop through the menu items to create buttons and dropdowns
        for (String[] itemGroup : menuItems) {
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout());
            menuPanel.setBackground(new Color(0, 0, 0));

            // Create main button for the menu item
            JButton mainButton = new JButton(itemGroup[0]);
            mainButton.setFont(new Font("Arial", Font.BOLD, 18));
            mainButton.setForeground(Color.WHITE);
            mainButton.setBackground(new Color(0, 0, 0));
            mainButton.setFocusPainted(false);
            mainButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            mainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Create dropdown panel to hold submenu items
            JPanel dropdownPanel = new JPanel();
            dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.Y_AXIS));
            dropdownPanel.setBackground(new Color(0, 0, 0));
            dropdownPanel.setVisible(false);

            // Loop through submenu items and add them as buttons
            for (int i = 1; i < itemGroup.length; i++) {
                JButton subButton = new JButton(itemGroup[i]);
                subButton.setFont(new Font("Arial", Font.PLAIN, 16));
                subButton.setForeground(Color.WHITE);
                subButton.setBackground(new Color(0, 0, 0));
                subButton.setFocusPainted(false);
                subButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));
                subButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dropdownPanel.add(subButton);
            }

            // Toggle visibility of dropdown panel when the main button is clicked
            mainButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dropdownPanel.setVisible(!dropdownPanel.isVisible());
                    sidebar.revalidate(); // Refresh sidebar layout
                    sidebar.repaint(); // Redraw sidebar
                }
            });

            // Add main button and dropdown panel to menu panel
            menuPanel.add(mainButton, BorderLayout.NORTH);
            menuPanel.add(dropdownPanel, BorderLayout.CENTER);
            sidebar.add(menuPanel);
        }

        // Sidebar Toggle Button
        JButton toggleButton = new JButton("☰");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 20));
        toggleButton.setBackground(new Color(199, 174, 106));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSidebarVisible = !isSidebarVisible;
                sidebar.setVisible(isSidebarVisible);
                revalidate(); // Refresh layout
                repaint(); // Redraw frame
            }
        });

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Header Panel to hold the toggle button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 0, 0));
        headerPanel.setPreferredSize(new Dimension(1200, 50));
        headerPanel.add(toggleButton, BorderLayout.WEST);

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
