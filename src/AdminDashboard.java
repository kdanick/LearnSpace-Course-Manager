import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DatabaseManager.Db_connect;
import analytics.*;

public class AdminDashboard extends JPanel {
    // Constructor for AdminDashboard
    public AdminDashboard() {
        // Set layout and background color for the panel
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // Create a panel for dashboard cards with a grid layout
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Add dashboard cards for various metrics
        cardsPanel.add(createDashboardCard("Courses", "SELECT COUNT(*) FROM Courses", Color.LIGHT_GRAY));
        cardsPanel.add(createDashboardCard("Students", "SELECT COUNT(*) FROM Students", Color.WHITE));
        cardsPanel.add(createDashboardCard("Enrollments", "SELECT COUNT(*) FROM Enrollments", Color.ORANGE));
        cardsPanel.add(createDashboardCard("Instructors", "SELECT COUNT(*) FROM Users WHERE role='lecturer'", Color.YELLOW));

        // Create a panel for analytics with GridBagLayout for flexible positioning
        JPanel analyticsPanel = new JPanel(new GridBagLayout());
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        analyticsPanel.setBackground(Color.LIGHT_GRAY);

        // Set up GridBagConstraints for positioning components in analyticsPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Create a row for charts and add them to the analytics panel
        JPanel chartRow = new JPanel(new GridLayout(1, 2, 20, 10));
        chartRow.setBackground(Color.LIGHT_GRAY);
        chartRow.setBorder(BorderFactory.createTitledBorder("Analytics Overview"));
        chartRow.setPreferredSize(new Dimension(800, 300));

        // Add the chart components to the chartRow
        chartRow.add(new StudentBarChart());
        chartRow.add(new CoursePieChart());

        // Add chartRow to the analytics panel
        gbc.gridy = 0;
        analyticsPanel.add(chartRow, gbc);

        // Add the course grade table to the analytics panel
        gbc.gridy = 1;
        analyticsPanel.add(new CourseGradeTable(), gbc);

        // Create the main content panel and add the cards and analytics panels
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(cardsPanel);
        mainContent.add(analyticsPanel);

        // Create a scroll pane for the main content to allow scrolling
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the scroll pane to the main panel
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to create a dashboard card with title and value
    private JPanel createDashboardCard(String title, String query, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setPreferredSize(new Dimension(150, 100));

        // Create and style the title label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create and get the value label from the database
        JLabel valueLabel = new JLabel(fetchCountFromDatabase(query), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        valueLabel.setForeground(Color.BLACK);

        // Add title and value labels to the card
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // Method to fetch a count value from the database using the provided query
    private String fetchCountFromDatabase(String query) {
        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1)); // Return the count as a string
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        }
        return "N/A"; // Return "N/A" if there was an error
    }
}