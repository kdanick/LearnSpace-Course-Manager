import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.Timer;
import DatabaseManager.Db_connect;
import analytics.*;

public class AdminDashboard extends JPanel {
    private ArrayList<JLabel> valueLabels = new ArrayList<>(); // Store card value labels
    private JPanel cardsPanel;

    public AdminDashboard(Integer user_id) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);


        cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        addDashboardCard("Courses", "SELECT COUNT(*) FROM Courses", Color.LIGHT_GRAY);
        addDashboardCard("Students", "SELECT COUNT(*) FROM Students", Color.WHITE);
        addDashboardCard("Enrollments", "SELECT COUNT(*) FROM Enrollments", Color.ORANGE);
        addDashboardCard("Instructors", "SELECT COUNT(*) FROM Users WHERE role='lecturer'", Color.YELLOW);


        JPanel analyticsPanel = new JPanel(new GridBagLayout());
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        analyticsPanel.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // **Analytics Row: Bar Chart & Pie Chart**
        JPanel chartRow = new JPanel(new GridLayout(1, 2, 20, 10));
        chartRow.setBackground(Color.LIGHT_GRAY);
        chartRow.setBorder(BorderFactory.createTitledBorder("Analytics Overview"));
        chartRow.setPreferredSize(new Dimension(800, 300));

        chartRow.add(new StudentBarChart());
        chartRow.add(new CoursePieChart());

        gbc.gridy = 0;
        analyticsPanel.add(chartRow, gbc);


        gbc.gridy = 1;
        analyticsPanel.add(new CourseGradeTable(), gbc);


        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(cardsPanel);
        mainContent.add(analyticsPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);


        startAutoRefresh();
    }


    private void addDashboardCard(String title, String query, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setPreferredSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel valueLabel = new JLabel(fetchCountFromDatabase(query), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        valueLabel.setForeground(Color.BLACK);

        valueLabels.add(valueLabel); // Store for later updates

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        cardsPanel.add(card);
    }


    private String fetchCountFromDatabase(String query) {
        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }


    private void startAutoRefresh() {
        Timer timer = new Timer(10_000, e -> updateDashboardCards());
        timer.start();
    }


    private void updateDashboardCards() {
        String[] queries = {
                "SELECT COUNT(*) FROM Courses",
                "SELECT COUNT(*) FROM Students",
                "SELECT COUNT(*) FROM Enrollments",
                "SELECT COUNT(*) FROM Users WHERE role='lecturer'"
        };

        for (int i = 0; i < valueLabels.size(); i++) {
            String newValue = fetchCountFromDatabase(queries[i]);
            valueLabels.get(i).setText(newValue);
        }
    }
}
