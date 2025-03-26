package analytics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseGradeTable extends JPanel {
    public CourseGradeTable() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Average Grade per Course"));

        String[] columns = {"Course", "Average Grade"};
        String[][] data = {
                {"Math", "B"},
                {"Science", "A"},
                {"History", "C"},
                {"Programming", "A"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        add(scrollPane, BorderLayout.CENTER);
    }
}
