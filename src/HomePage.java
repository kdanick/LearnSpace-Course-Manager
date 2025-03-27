import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DatabaseManager.Db_connect;
import analytics.LecCoursePieChart;
import analytics.LecturerCourseTable;
import analytics.LecturerEnrollmentTable;

public class HomePage extends JPanel {
    public HomePage(int lecturerId) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // **TOP PANEL: Dashboard Cards**
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        cardsPanel.add(createDashboardCard("Assigned Courses", "SELECT COUNT(*) FROM Courses WHERE lecturer_id = " + lecturerId, Color.BLUE));
        cardsPanel.add(createDashboardCard(
                "Total Students Enrolled",
                "SELECT COUNT(DISTINCT student_id) FROM Enrollments WHERE course_id IN (SELECT course_id FROM Courses WHERE lecturer_id = " + lecturerId + ")",
                Color.YELLOW.darker()));

// For "Overall Grade", query the average grade of all students in the courses taught by this lecturer
        cardsPanel.add(createDashboardCard(
                "Overall Grade",
                "SELECT AVG(g.score) FROM Grades g " +
                        "JOIN Courses c ON g.course_id = c.course_id " +
                        "WHERE c.lecturer_id = " + lecturerId,
                Color.GREEN.darker()));

        // **MIDDLE PANEL: Course Table + Pie Chart**
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 0, 0));  // Reduced horizontal gap to 5px, no vertical gap
        middlePanel.setBackground(Color.LIGHT_GRAY);
        middlePanel.setPreferredSize(new Dimension(900, 300));  // Adjust the width of the middle panel for more space
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        // Left: Course Table
        JPanel leftPanel = new JPanel(new BorderLayout());  // Use BorderLayout for better flexibility
        LecturerCourseTable courseTable = new LecturerCourseTable(lecturerId);
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));  // Removed extra padding
        leftPanel.add(courseTable, BorderLayout.CENTER);  // Add the course table to the center

        // Right: Pie Chart (LecCoursePieChart)
        JPanel rightPanel = new JPanel();  // Simple panel for the pie chart
        LecCoursePieChart pieChart = new LecCoursePieChart(lecturerId);
        pieChart.setPreferredSize(new Dimension(400, 250));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));  // Removed extra padding
        rightPanel.add(pieChart);  // Add pie chart to the panel

        // Add both left and right panels to the middle panel
        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);

        // **BOTTOM PANEL: Enrollments Table**
        JPanel enrollmentsPanel = new JPanel(new BorderLayout());
        enrollmentsPanel.setBorder(BorderFactory.createTitledBorder("Course Enrollments"));
         enrollmentsPanel.add(new LecturerEnrollmentTable(lecturerId), BorderLayout.CENTER);

        // **Main Content Panel (Scrollable)**
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(cardsPanel);
        mainContent.add(middlePanel);
        mainContent.add(enrollmentsPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    // **DASHBOARD CARDS**
    private JPanel createDashboardCard(String title, String query, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel valueLabel = new JLabel(fetchCountFromDatabase(query), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        valueLabel.setForeground(Color.BLACK);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
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
}
