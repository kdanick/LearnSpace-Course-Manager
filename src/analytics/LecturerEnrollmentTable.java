package analytics;

import DatabaseManager.Db_connect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class LecturerEnrollmentTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private int lecturerId;

    public LecturerEnrollmentTable(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Students Enrolled in Your Courses"));

        // Define table columns
        String[] columns = {"baseClasses.Student ID", "baseClasses.Student Name", "Course ID", "Course Name", "Enrolled On"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Add table inside scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        add(scrollPane, BorderLayout.CENTER);

        refreshData(); // Initial data load
        startAutoRefresh(); // Enable auto-refresh
    }

    private void refreshData() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear table before updating

            String query = """
                SELECT e.student_id, s.name AS student_name, e.course_id, c.course_name, e.enrolled_on
                FROM Enrollments e
                JOIN Students s ON e.student_id = s.student_id
                JOIN Courses c ON e.course_id = c.course_id
                WHERE c.lecturer_id = ?
                ORDER BY c.course_name, s.name;
            """;

            try (Connection conn = Db_connect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, lecturerId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    String studentName = rs.getString("student_name");
                    String courseId = rs.getString("course_id");
                    String courseName = rs.getString("course_name");
                    String enrolledOn = rs.getTimestamp("enrolled_on").toString();

                    tableModel.addRow(new Object[]{studentId, studentName, courseId, courseName, enrolledOn});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching enrollment data from database!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startAutoRefresh() {
        Timer timer = new Timer(true); // Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        }, 0, 5000); // Refresh every 5 seconds
    }
}
