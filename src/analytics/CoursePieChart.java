package analytics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;

public class CoursePieChart extends JPanel {
    public CoursePieChart() {
        setLayout(new BorderLayout());

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Maths", 30);
        dataset.setValue("Computer Science", 45);
        dataset.setValue("English", 25);

        JFreeChart pieChart = ChartFactory.createPieChart("Grade Distribution", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        add(chartPanel, BorderLayout.CENTER);
    }
}
