package analytics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DatabaseManager.Db_connect;
import java.util.Timer;
import java.util.TimerTask;

public class LecturerCourseTable extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private int lecturerId;
    private Timer timer;

    public LecturerCourseTable(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());

        // Create the table model
        model = new DefaultTableModel();
        model.addColumn("Course ID");
        model.addColumn("Course Name");
        model.addColumn("Enrolled Students");
        model.addColumn("Average Score");

        // Create the JTable with the model
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        add(new JScrollPane(table), BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Course Overview"));
        setPreferredSize(new Dimension(800, 300));

        // Load initial data
        fetchData();

        // Start periodic refresh
        startAutoRefresh();
    }

    private void fetchData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
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

                try (Connection conn = Db_connect.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, lecturerId);
                    ResultSet rs = pstmt.executeQuery();

                    // Clear table before updating
                    model.setRowCount(0);

                    while (rs.next()) {
                        String courseId = rs.getString("course_id");
                        String courseName = rs.getString("course_name");
                        int enrolledStudents = rs.getInt("enrolled_students");
                        double averageScore = rs.getDouble("average_score");

                        model.addRow(new Object[]{courseId, courseName, enrolledStudents, averageScore});
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void startAutoRefresh() {
        timer = new Timer(true); // Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchData();
            }
        }, 5000, 5000); // Refresh every 5 seconds
    }

    public void stopAutoRefresh() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
