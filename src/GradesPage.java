import DatabaseManager.Db_connect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class GradesPage extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> courseDropdown;
    private int lecturerId;

    public GradesPage(int lecturerId) {
        this.lecturerId = lecturerId;
        setLayout(new BorderLayout());

        // Top Panel for Course Filtering
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course: "));
        courseDropdown = new JComboBox<>();
        loadCourses();
        courseDropdown.addActionListener(e -> refreshTable());
        topPanel.add(courseDropdown);
        add(topPanel, BorderLayout.NORTH);

        // Table Setup
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Student Name", "Course ID", "Score", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton insertButton = new JButton("Insert Grade");
        JButton updateButton = new JButton("Update Grade");
        JButton deleteButton = new JButton("Delete Grade");

        insertButton.addActionListener(e -> insertGrade());
        updateButton.addActionListener(e -> updateGrade());
        deleteButton.addActionListener(e -> deleteGrade());

        bottomPanel.add(insertButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void loadCourses() {
        try (Connection conn = Db_connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT course_id, course_name FROM courses WHERE lecturer_id = ?")) {
            stmt.setInt(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            courseDropdown.addItem("Select Course");
            while (rs.next()) {
                courseDropdown.addItem(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String selectedCourse = (String) courseDropdown.getSelectedItem();
        if (selectedCourse == null || selectedCourse.equals("Select Course")) return;

        try (Connection conn = Db_connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.course_id, s.student_id, s.name, g.score, g.grade " +
                             "FROM students s " +
                             "JOIN enrollments e ON s.student_id = e.student_id " +
                             "JOIN courses c ON e.course_id = c.course_id " +
                             "LEFT JOIN grades g ON g.student_id = s.student_id AND g.course_id = c.course_id " +
                             "WHERE c.course_name = ?")) {
            stmt.setString(1, selectedCourse);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("student_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("course_id"));
                row.add(rs.getObject("score"));
                row.add(rs.getString("grade"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertGrade() {
        JTextField studentIdField = new JTextField();
        JTextField scoreField = new JTextField();
        String selectedCourse = (String) courseDropdown.getSelectedItem();
        if (selectedCourse == null || selectedCourse.equals("Select Course")) {
            JOptionPane.showMessageDialog(this, "Please select a course first.");
            return;
        }

        Object[] message = {"Student ID:", studentIdField, "Score:", scoreField};
        int option = JOptionPane.showConfirmDialog(this, message, "Insert Grade", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = Db_connect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO grades (student_id, course_id, score) VALUES (?, (SELECT course_id FROM courses WHERE course_name = ?), ?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, Integer.parseInt(studentIdField.getText()));
                stmt.setString(2, selectedCourse);
                stmt.setDouble(3, Double.parseDouble(scoreField.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Grade inserted successfully.");
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting grade: " + e.getMessage());
            }
        }
    }

    private void updateGrade() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to update.");
            return;
        }

        int studentId = (int) table.getValueAt(selectedRow, 0);
        JTextField scoreField = new JTextField(table.getValueAt(selectedRow, 3).toString());

        Object[] message = {"New Score:", scoreField};
        int option = JOptionPane.showConfirmDialog(this, message, "Update Grade", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = Db_connect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE grades SET score = ? WHERE student_id = ? AND course_id = ?")) {
                stmt.setDouble(1, Double.parseDouble(scoreField.getText()));
                stmt.setInt(2, studentId);
                stmt.setString(3, table.getValueAt(selectedRow, 2).toString());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Grade updated successfully.");
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating grade: " + e.getMessage());
            }
        }
    }

    private void deleteGrade() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to delete.");
            return;
        }

        int studentId = (int) table.getValueAt(selectedRow, 0);
        String courseId = (String) table.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this grade?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Db_connect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM grades WHERE student_id = ? AND course_id = ?")) {
                stmt.setInt(1, studentId);
                stmt.setString(2, courseId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Grade deleted successfully.");
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting grade: " + e.getMessage());
            }
        }
    }
}
