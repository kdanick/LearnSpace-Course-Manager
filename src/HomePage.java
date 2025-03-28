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
    private JLabel assignedCoursesLabel;
    private JLabel totalStudentsLabel;
    private JLabel overallGradeLabel;

    public HomePage(int lecturerId) {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(Color.LIGHT_GRAY);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        assignedCoursesLabel = new JLabel("Loading...", SwingConstants.CENTER);
        totalStudentsLabel = new JLabel("Loading...", SwingConstants.CENTER);
        overallGradeLabel = new JLabel("Loading...", SwingConstants.CENTER);

        cardsPanel.add(createDashboardCard("Assigned Courses", assignedCoursesLabel, Color.BLUE));
        cardsPanel.add(createDashboardCard("Total Students Enrolled", totalStudentsLabel, Color.YELLOW.darker()));
        cardsPanel.add(createDashboardCard("Overall Grade", overallGradeLabel, Color.GREEN.darker()));

        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 0, 0));
        middlePanel.setBackground(Color.LIGHT_GRAY);
        middlePanel.setPreferredSize(new Dimension(900, 300));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        LecturerCourseTable courseTable = new LecturerCourseTable(lecturerId);
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        leftPanel.add(courseTable, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        LecCoursePieChart pieChart = new LecCoursePieChart(lecturerId);
        pieChart.setPreferredSize(new Dimension(400, 250));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rightPanel.add(pieChart);

        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);

        JPanel enrollmentsPanel = new JPanel(new BorderLayout());
        enrollmentsPanel.setBorder(BorderFactory.createTitledBorder("Course Enrollments"));
        enrollmentsPanel.add(new LecturerEnrollmentTable(lecturerId), BorderLayout.CENTER);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(cardsPanel);
        mainContent.add(middlePanel);
        mainContent.add(enrollmentsPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // Start auto-refresh
        startAutoRefresh(lecturerId);
    }

    private JPanel createDashboardCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setPreferredSize(new Dimension(200, 100));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        valueLabel.setForeground(Color.BLACK);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void startAutoRefresh(int lecturerId) {
        Timer timer = new Timer(5000, e -> refreshData(lecturerId)); // Refresh every 5 seconds
        timer.start();
    }

    private void refreshData(int lecturerId) {
        assignedCoursesLabel.setText(fetchCountFromDatabase("SELECT COUNT(*) FROM Courses WHERE lecturer_id = " + lecturerId));
        totalStudentsLabel.setText(fetchCountFromDatabase(
                "SELECT COUNT(DISTINCT student_id) FROM Enrollments WHERE course_id IN " +
                        "(SELECT course_id FROM Courses WHERE lecturer_id = " + lecturerId + ")"));
        overallGradeLabel.setText(fetchCountFromDatabase(
                "SELECT AVG(g.score) FROM Grades g " +
                        "JOIN Courses c ON g.course_id = c.course_id " +
                        "WHERE c.lecturer_id = " + lecturerId));
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
