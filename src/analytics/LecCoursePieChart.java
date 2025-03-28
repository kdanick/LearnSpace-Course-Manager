package analytics;

import DatabaseManager.Db_connect;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class LecCoursePieChart extends JPanel {
    private DefaultPieDataset dataset;
    private JFreeChart pieChart;
    private ChartPanel chartPanel;
    private int lecturerId;

    public LecCoursePieChart(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());

        dataset = new DefaultPieDataset();
        pieChart = ChartFactory.createPieChart("Student Distribution per Course", dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        chartPanel = new ChartPanel(pieChart);
        add(chartPanel, BorderLayout.CENTER);

        refreshData(); // Initial data load
        startAutoRefresh(); // Enable auto-refresh
    }

    private void refreshData() {
        dataset.clear();
        String query = """
            SELECT c.course_name, COUNT(e.student_id) AS student_count
            FROM Courses c
            LEFT JOIN Enrollments e ON c.course_id = e.course_id
            WHERE c.lecturer_id = ?
            GROUP BY c.course_name;
        """;

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, lecturerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                dataset.setValue(rs.getString("course_name"), rs.getInt("student_count"));
            }

            pieChart.fireChartChanged(); // Update chart after refreshing data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading chart!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startAutoRefresh() {
        Timer timer = new Timer(true); // Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> refreshData());
            }
        }, 0, 5000); // Refresh every 5 seconds
    }
}
