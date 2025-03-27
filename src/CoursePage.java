import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;
import Round.RoundedButton;

public class CoursePage extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public CoursePage() {
        setLayout(new BorderLayout());

        // Top Panel with Title
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Courses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Table Model
        tableModel = new DefaultTableModel();
        courseTable = new JTable(tableModel);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        courseTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load Courses
        loadCourses();

        // Buttons
        JPanel buttonPanel = new JPanel();
        RoundedButton insertButton = new RoundedButton("Add Course");
        RoundedButton updateButton = new RoundedButton("Edit Course");
        RoundedButton deleteButton = new RoundedButton("Remove Course");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(this::insertCourse);
        updateButton.addActionListener(this::updateCourse);
        deleteButton.addActionListener(this::deleteCourse);
    }

    private void loadCourses() {
        try (Connection connection = Db_connect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT c.course_id, c.course_name, c.credits, " +
                             "COALESCE(u.name, 'None') AS lecturer_name " +
                             "FROM Courses c " +
                             "LEFT JOIN Users u ON c.lecturer_id = u.user_id ")) {

            tableModel.setRowCount(0); // Clear previous data
            tableModel.setColumnIdentifiers(new String[]{"Course ID", "Course Name", "Credits", "Lecturer"});

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getInt("credits"),
                        resultSet.getString("lecturer_name")
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses.");
            e.printStackTrace();
        }
    }



    private void insertCourse(ActionEvent e) {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField();
        JTextField lecturerField = new JTextField();

        Object[] message = {
                "Course ID:", idField,
                "Course Name:", nameField,
                "Credits (0-3):", creditField,
                "Lecturer ID:", lecturerField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enter Course Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String creditsText = creditField.getText().trim();
            String lecturerId = lecturerField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || creditsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except Lecturer ID must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int credits = Integer.parseInt(creditsText);
                if (credits < 0 || credits > 3) {
                    JOptionPane.showMessageDialog(this, "Credits must be between 0 and 3!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO Courses (course_id, course_name, credits, lecturer_id) VALUES (?, ?, ?, ?)")) {

                    statement.setString(1, id);
                    statement.setString(2, name);
                    statement.setInt(3, credits);
                    if (lecturerId.isEmpty()) {
                        statement.setNull(4, Types.INTEGER);
                    } else {
                        statement.setInt(4, Integer.parseInt(lecturerId));
                    }

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Course added successfully.");
                    loadCourses();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits and Lecturer ID must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting course: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void updateCourse(ActionEvent e) {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to update.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        int currentCredits = (int) tableModel.getValueAt(selectedRow, 2);
        Object currentLecturer = tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField creditField = new JTextField(String.valueOf(currentCredits));
        JTextField lecturerField = new JTextField(currentLecturer != null ? currentLecturer.toString() : "");

        Object[] message = {
                "Course Name:", nameField,
                "Credits (0-3):", creditField,
                "Lecturer ID:", lecturerField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Course Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String creditsText = creditField.getText().trim();
            String lecturerId = lecturerField.getText().trim();

            if (newName.isEmpty() || creditsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except Lecturer ID must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int newCredits = Integer.parseInt(creditsText);
                if (newCredits < 0 || newCredits > 3) {
                    JOptionPane.showMessageDialog(this, "Credits must be between 0 and 3!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "UPDATE Courses SET course_name=?, credits=?, lecturer_id=? WHERE course_id=?")) {

                    statement.setString(1, newName);
                    statement.setInt(2, newCredits);
                    if (lecturerId.isEmpty()) {
                        statement.setNull(3, Types.INTEGER);
                    } else {
                        statement.setInt(3, Integer.parseInt(lecturerId));
                    }
                    statement.setString(4, courseId);

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Course updated successfully.");
                    loadCourses();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits and Lecturer ID must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating course.");
                ex.printStackTrace();
            }
        }
    }

    private void deleteCourse(ActionEvent e) {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to delete.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this course?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Courses WHERE course_id=?")) {

                statement.setString(1, courseId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course removed successfully.");
                loadCourses();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting course.");
                ex.printStackTrace();
            }
        }
    }
}