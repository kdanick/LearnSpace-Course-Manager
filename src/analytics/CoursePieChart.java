package analytics;

import DatabaseManager.Db_connect;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import java.awt.Font;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoursePieChart extends JPanel {
    public CoursePieChart() {
        setLayout(new BorderLayout());
        DefaultPieDataset dataset = fetchDataFromDatabase();

        JFreeChart pieChart = ChartFactory.createPieChart("Student Distribution per Course", dataset, true, true, false);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        LegendTitle legend = pieChart.getLegend();
        legend.setItemFont(new Font("SansSerif", Font.BOLD, 12));

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        add(chartPanel, BorderLayout.CENTER);
    }

    private DefaultPieDataset fetchDataFromDatabase() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String query = """
            SELECT c.course_name, COUNT(e.student_id) AS student_count
            FROM Courses c
            LEFT JOIN Enrollments e ON c.course_id = e.course_id
            GROUP BY c.course_name;
        """;

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String courseName = rs.getString("course_name");
                int studentCount = rs.getInt("student_count");
                dataset.setValue(courseName, studentCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from database!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return dataset;
    }
}
