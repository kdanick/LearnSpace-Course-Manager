package analytics;

import DatabaseManager.Db_connect;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentBarChart extends JPanel {
    public StudentBarChart() {
        setLayout(new BorderLayout());

        // Fetch data from the database
        DefaultCategoryDataset dataset = fetchDataFromDatabase();

        // Create the bar chart
        JFreeChart barChart = ChartFactory.createBarChart("Students per Course", "Course", "Students", dataset);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultCategoryDataset fetchDataFromDatabase() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // SQL query to count students per course
        String query = """
        SELECT c.course_name, COUNT(e.student_id) AS student_count
        FROM Courses c
        LEFT JOIN Enrollments e ON c.course_id = e.course_id
        GROUP BY c.course_name
        ORDER BY c.course_name;
        """;

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                int studentCount = rs.getInt("student_count");
                dataset.addValue(studentCount, "Students", courseName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching student enrollment data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }
}
