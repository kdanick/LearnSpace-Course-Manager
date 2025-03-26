package analytics;

import DatabaseManager.Db_connect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseGradeTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public CourseGradeTable() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Average Grade per Course"));

        // Define table columns
        String[] columns = {"course_id", "Course", "Average Grade", "Average Score"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Add table inside scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        add(scrollPane, BorderLayout.CENTER);

        // Fetch data from database
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        String query = """
        SELECT DISTINCT c.course_id, c.course_name, gs.grade, avg_grades.avg_score
        FROM Courses c
        JOIN (
            SELECT course_id, AVG(score) AS avg_score
            FROM Grades
            GROUP BY course_id
        ) AS avg_grades ON c.course_id = avg_grades.course_id
        JOIN GradeScale gs ON avg_grades.avg_score BETWEEN gs.min_score AND gs.max_score
        ORDER BY c.course_name;
        
        """;

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String course_id = rs.getString("course_id");
                String course = rs.getString("course_name");
                String grade = rs.getString("grade");
                double score = rs.getDouble("avg_score");
                tableModel.addRow(new Object[]{course_id, course, grade, score});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from database!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
