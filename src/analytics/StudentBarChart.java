package analytics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;

public class StudentBarChart extends JPanel {
    public StudentBarChart() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(120, "Students", "Math");
        dataset.addValue(80, "Students", "Science");
        dataset.addValue(95, "Students", "History");
        dataset.addValue(110, "Students", "Programming");

        JFreeChart barChart = ChartFactory.createBarChart("Students per Course", "Course", "Students", dataset);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        add(chartPanel, BorderLayout.CENTER);
    }
}
