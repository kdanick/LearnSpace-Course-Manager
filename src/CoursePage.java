import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePage extends JPanel {
    public CoursePage() {
        setBackground(Color.WHITE); // White background
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE); // White background

        JLabel greetingLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        greetingLabel.setForeground(Color.BLACK); // Black text

        JLabel subGreetingLabel = new JLabel("Manage your courses efficiently.", SwingConstants.CENTER);
        subGreetingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subGreetingLabel.setForeground(Color.BLACK); // Black text

        topPanel.add(greetingLabel);
        topPanel.add(subGreetingLabel);
        add(topPanel, BorderLayout.NORTH);

        // Table Data
        String[] columnNames = {"Course ID", "Course Name", "Lecturer Name", "Lecturer ID"};
        Object[][] courseData = {
                {201, "Mathematics", "Dr. Alice Johnson", 301},
                {202, "Computer Science", "Prof. Bob Smith", 302},
                {203, "History", "Dr. Charlie Brown", 303}
        };

        JTable table = new JTable(new DefaultTableModel(courseData, columnNames));
        table.setBackground(Color.WHITE); // White background
        table.setForeground(Color.BLACK); // Black text
        table.setGridColor(Color.BLACK); // Black grid lines
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JLabel label2 = new JLabel("List of Available Courses", SwingConstants.CENTER);
        label2.setFont(new Font("Arial", Font.BOLD, 18));
        label2.setForeground(Color.BLACK); // Black text
        add(label2, BorderLayout.SOUTH);
    }
}
