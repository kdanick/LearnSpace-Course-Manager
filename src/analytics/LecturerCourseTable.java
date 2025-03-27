package analytics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DatabaseManager.Db_connect;

public class LecturerCourseTable extends JPanel {

    public LecturerCourseTable(int lecturerId) {
        setLayout(new BorderLayout());  // Use BorderLayout for better flexibility

        // Create the table model and set column names
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Course ID");
        model.addColumn("Course Name");
        model.addColumn("Enrolled Students");
        model.addColumn("Average Score");

    // SQL query to get courses, average score, and number of students for the given lecturer
        String sql = """
            SELECT 
                c.course_id, 
                c.course_name, 
                COUNT(DISTINCT e.student_id) AS enrolled_students, 
                COALESCE(AVG(g.score), 0) AS average_score
            FROM 
                Courses c
            JOIN 
                Enrollments e ON e.course_id = c.course_id
            LEFT JOIN 
                Grades g ON g.course_id = c.course_id AND g.student_id = e.student_id
            WHERE 
                c.lecturer_id = ?
            GROUP BY 
                c.course_id, c.course_name
            ORDER BY 
                c.course_name;
        """;


        // Fetch data and populate the table
        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lecturerId); // Set the lecturer_id parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String courseId = rs.getString("course_id");
                String courseName = rs.getString("course_name");
                int enrolledStudents = rs.getInt("enrolled_students");
                double averageScore = rs.getDouble("average_score");

                // Add the row to the table model
                model.addRow(new Object[]{courseId, courseName, enrolledStudents, averageScore});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create the JTable with the model
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true); // Ensure it fits in the parent panel

        // Set some basic table properties
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);  // Course ID column width
        table.getColumnModel().getColumn(1).setPreferredWidth(140); // Course Name column width
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Enrolled Students column width
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Average Score column width

        // Add the table to the panel (no JScrollPane, let it fill the space)
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Set a title with TitledBorder for the table
        setBorder(BorderFactory.createTitledBorder("Course Overview"));

        // Set a larger preferred size for the table (increased height and width)
        setPreferredSize(new Dimension(800, 300)); // Adjust to your desired size
    }
}
