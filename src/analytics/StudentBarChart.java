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
import java.util.Timer;
import java.util.TimerTask;

public class StudentBarChart extends JPanel {
    private DefaultCategoryDataset dataset;
    private JFreeChart barChart;
    private ChartPanel chartPanel;
    private Timer timer;

    public StudentBarChart() {
        setLayout(new BorderLayout());

        dataset = new DefaultCategoryDataset();
        barChart = ChartFactory.createBarChart("Students per Course", "Course", "Students", dataset);
        chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        add(chartPanel, BorderLayout.CENTER);
        refreshData();
        startAutoRefresh();
    }

    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            dataset.clear();

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
        });
    }

    private void startAutoRefresh() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        }, 0, 5000);
    }

    public void stopAutoRefresh() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
