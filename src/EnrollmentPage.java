import DatabaseManager.Db_connect;
import Round.RoundedButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentPage extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private int lecturerId;
    private JTextField searchField;

    public EnrollmentPage(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("baseClasses.Student Enrollments"));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(15);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Define table columns
        String[] columns = {"baseClasses.Student ID", "baseClasses.Student Name", "Course ID", "Course Name", "Enrolled On"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        RoundedButton insertButton = new RoundedButton("Insert Enrollment");
        RoundedButton updateButton = new RoundedButton("Update Enrollment");
        RoundedButton deleteButton = new RoundedButton("Delete Enrollment");

        insertButton.addActionListener(e -> insertEnrollment());
        updateButton.addActionListener(e -> updateEnrollment());
        deleteButton.addActionListener(e -> deleteEnrollment());

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch data
        fetchDataFromDatabase();

        // Add search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });
    }

    private void fetchDataFromDatabase() {
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

            tableModel.setRowCount(0); // Clear table before inserting new rows
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
            JOptionPane.showMessageDialog(this, "Error fetching enrollment data!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    private void insertEnrollment() {
        JTextField studentIdField = new JTextField();
        JTextField courseIdField = new JTextField();

        Object[] message = {
                "baseClasses.Student ID:", studentIdField,
                "Course ID:", courseIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Insert Enrollment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String courseId = courseIdField.getText();
            int studentId = Integer.parseInt(studentIdField.getText());

            try (Connection conn = Db_connect.getConnection()) {
                // Check if the course is taught by the lecturer
                String checkQuery = "SELECT COUNT(*) FROM Courses WHERE course_id = ? AND lecturer_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, courseId);
                    checkStmt.setInt(2, lecturerId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        // If course exists under lecturer, insert enrollment
                        String insertQuery = "INSERT INTO Enrollments (student_id, course_id, enrolled_on) VALUES (?, ?, NOW())";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                            pstmt.setInt(1, studentId);
                            pstmt.setString(2, courseId);
                            pstmt.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Enrollment added successfully.");
                            refreshTable();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "You are not authorized to enroll students in this course.",
                                "Permission Denied", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting enrollment: " + e.getMessage());
            }
        }
    }

    private void updateEnrollment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String courseId = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField newCourseIdField = new JTextField(courseId);

        Object[] message = {
                "New Course ID:", newCourseIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Enrollment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newCourseId = newCourseIdField.getText();

            try (Connection conn = Db_connect.getConnection()) {
                // Check if the new course belongs to the same lecturer
                String checkQuery = "SELECT COUNT(*) FROM Courses WHERE course_id = ? AND lecturer_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, newCourseId);
                    checkStmt.setInt(2, lecturerId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        // Perform the update
                        String updateQuery = "UPDATE Enrollments SET course_id = ? WHERE student_id = ? AND course_id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                            pstmt.setString(1, newCourseId);
                            pstmt.setInt(2, studentId);
                            pstmt.setString(3, courseId);
                            pstmt.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Enrollment updated successfully.");
                            refreshTable();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "You are not authorized to assign this course.",
                                "Permission Denied", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating enrollment: " + e.getMessage());
            }
        }
    }

    private void deleteEnrollment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String courseId = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this enrollment?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Db_connect.getConnection()) {
                String deleteQuery = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                    pstmt.setInt(1, studentId);
                    pstmt.setString(2, courseId);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Enrollment deleted successfully.");
                    refreshTable();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting enrollment: " + e.getMessage());
            }
        }
    }


    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            fetchDataFromDatabase();
            tableModel.fireTableDataChanged(); // Notify table of data changes
        });
    }
}
