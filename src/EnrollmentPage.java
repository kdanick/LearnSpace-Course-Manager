import DatabaseManager.Db_connect;
import Round.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentPage extends JPanel {
    private JTable table; // Make table accessible for refreshing

    public EnrollmentPage() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Top Panel with Title
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.WHITE); // White background

        JLabel titleLabel = new JLabel("Enrollment Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK); // Black text

        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Fetch enrollment data from the database
        List<Enrollment> enrollments = getAllEnrollments();

        // Prepare table data
        String[] columnNames = {"Enrollment ID", "Student ID", "Course ID", "Enrolled On"};
        Object[][] enrollmentData = new Object[enrollments.size()][columnNames.length];

        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            enrollmentData[i][0] = enrollment.getEnrollmentId();
            enrollmentData[i][1] = enrollment.getStudentId();
            enrollmentData[i][2] = enrollment.getCourseId();
            enrollmentData[i][3] = enrollment.getEnrolledOn();
        }

        table = new JTable(new DefaultTableModel(enrollmentData, columnNames));
        table.setBackground(Color.WHITE); // White background
        table.setForeground(Color.BLACK); // Black text
        table.setGridColor(Color.BLACK); // Black grid lines
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for enrollment actions
        JPanel buttonPanel = new JPanel();
        RoundedButton addEnrollmentButton = new RoundedButton("Add Enrollment");
        RoundedButton deleteEnrollmentButton = new RoundedButton("Delete Enrollment");

        // Add action listeners for buttons
        addEnrollmentButton.addActionListener(e -> openEnrollmentDialog(null)); // For adding an enrollment

        deleteEnrollmentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String enrollmentId = (String) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this enrollment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteEnrollment(enrollmentId); // Call method to delete enrollment
                    refreshTable(); // Refresh table after deletion
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.");
            }
        });

        buttonPanel.add(addEnrollmentButton);
        buttonPanel.add(deleteEnrollmentButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openEnrollmentDialog(Enrollment enrollment) {
        JDialog dialog = new JDialog();
        dialog.setTitle(enrollment == null ? "Add Enrollment" : "Edit Enrollment");
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2));

        JTextField enrollmentIdField = new JTextField(enrollment != null ? enrollment.getEnrollmentId() : "");
        JTextField studentIdField = new JTextField(enrollment != null ? enrollment.getStudentId() : "");
        JTextField courseIdField = new JTextField(enrollment != null ? enrollment.getCourseId() : "");
        JTextField enrolledOnField = new JTextField(enrollment != null ? enrollment.getEnrolledOn() : "");

        dialog.add(new JLabel("Enrollment ID:"));
        dialog.add(enrollmentIdField);
        dialog.add(new JLabel("Student ID:"));
        dialog.add(studentIdField);
        dialog.add(new JLabel("Course ID:"));
        dialog.add(courseIdField);
        dialog.add(new JLabel("Enrolled On:"));
        dialog.add(enrolledOnField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (enrollment == null) {
                addEnrollment(enrollmentIdField.getText(), studentIdField.getText(), courseIdField.getText(), enrolledOnField.getText());
            }
            dialog.dispose(); // Close the dialog
            refreshTable(); // Refresh the table after adding/editing
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void addEnrollment(String enrollmentId, String studentId, String courseId, String enrolledOn) {
        String sql = "INSERT INTO enrollments (enrollment_id, student_id, course_id, enrolled_on) VALUES (?, ?, ?, ?)";

        // Input validation
        if (studentId.isEmpty() || courseId.isEmpty() || enrolledOn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            pstmt.setString(2, studentId);
            pstmt.setString(3, courseId);
            pstmt.setString(4, enrolledOn);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Enrollment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error adding enrollment: " + e.getMessage());
        }
    }

    private void deleteEnrollment(String enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enrollmentId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Enrollment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting enrollment: " + e.getMessage());
        }
    }

    private void refreshTable() {
        List<Enrollment> enrollments = getAllEnrollments();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Enrollment enrollment : enrollments) {
            model.addRow(new Object[]{
                    enrollment.getEnrollmentId(),
                    enrollment.getStudentId(),
                    enrollment.getCourseId(),
                    enrollment.getEnrolledOn()
            });
        }
    }

    private List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT enrollment_id, student_id, course_id, enrolled_on FROM enrollments";

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String enrollmentId = rs.getString("enrollment_id");
                String studentId = rs.getString("student_id");
                String courseId = rs.getString("course_id");
                String enrolledOn = rs.getString("enrolled_on");
                enrollments.add(new Enrollment(enrollmentId, studentId, courseId, enrolledOn));
            }
            System.out.println("Fetched enrollments: " + enrollments.size()); // Debugging line
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    // Inner Enrollment class
    private class Enrollment {
        private String enrollmentId;
        private String studentId;
        private String courseId;
        private String enrolledOn;

        public Enrollment(String enrollmentId, String studentId, String courseId, String enrolledOn) {
            this.enrollmentId = enrollmentId;
            this.studentId = studentId;
            this.courseId = courseId;
            this.enrolledOn = enrolledOn;
        }

        // Getters
        public String getEnrollmentId() { return enrollmentId; }
        public String getStudentId() { return studentId; }
        public String getCourseId() { return courseId; }
        public String getEnrolledOn() { return enrolledOn; }
    }
}