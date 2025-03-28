package analytics;

import DatabaseManager.Db_connect;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecCoursePieChart extends JPanel {
    public LecCoursePieChart(int lecturerId) {
        setLayout(new BorderLayout());
        DefaultPieDataset dataset = fetchDataFromDatabase(lecturerId);

        JFreeChart pieChart = ChartFactory.createPieChart("Student Distribution per Course", dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        ChartPanel chartPanel = new ChartPanel(pieChart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultPieDataset fetchDataFromDatabase(int lecturerId) {
        DefaultPieDataset dataset = new DefaultPieDataset();
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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading chart!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }

}
