import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;
import Round.RoundedButton;

public class StudentsPage extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton insertButton, updateButton, deleteButton;

    public StudentsPage() {
        setLayout(new BorderLayout());

        // Table Model
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load Students
        loadStudents();

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        RoundedButton insertButton = new RoundedButton("Insert");
        RoundedButton updateButton = new RoundedButton("Update");
        RoundedButton deleteButton = new RoundedButton("Delete");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(this::showInsertDialog);
        updateButton.addActionListener(this::showUpdateDialog);
        deleteButton.addActionListener(this::deleteStudent);
    }

    private void loadStudents() {
        try (Connection connection = Db_connect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            tableModel.setColumnIdentifiers(columnNames);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading student data.");
            e.printStackTrace();
        }
    }

    private void showInsertDialog(ActionEvent e) {
        showStudentDialog(null);
    }

    private void showUpdateDialog(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }
        int studentId = (int) tableModel.getValueAt(selectedRow, 0); // Assuming student ID is in the first column
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String email = (String) tableModel.getValueAt(selectedRow, 2);
        String course = (String) tableModel.getValueAt(selectedRow, 3);

        showStudentDialog(new Student(studentId, name, email, course));
    }

    private void showStudentDialog(Student student) {
        JDialog dialog = new JDialog();
        dialog.setTitle(student == null ? "Insert Student" : "Update Student");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2)); // Updated to 5 rows

        JTextField idField = new JTextField(student != null ? String.valueOf(student.getId()) : "");
        JTextField nameField = new JTextField(student != null ? student.getName() : "");
        JTextField emailField = new JTextField(student != null ? student.getEmail() : "");
        JTextField courseField = new JTextField(student != null ? student.getCourse() : "");

        dialog.add(new JLabel("Student ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Course:"));
        dialog.add(courseField);

        JButton saveButton = new JButton(student == null ? "Insert" : "Update");
        saveButton.addActionListener(e -> {
            if (student == null) {
                try {
                    int studentId = Integer.parseInt(idField.getText().trim());
                    insertStudent(studentId, nameField.getText(), emailField.getText(), courseField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Student ID. Please enter a valid integer.");
                }
            } else {
                updateStudent(student.getId(), nameField.getText(), emailField.getText(), courseField.getText());
            }
            dialog.dispose(); // Close the dialog
        });

        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    private void insertStudent(int studentId, String name, String email, String course) {
        if (name.isEmpty() || email.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        String sql = "INSERT INTO students (student_id, name, email, course) VALUES (?, ?, ?, ?)";
        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, course);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error inserting student: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateStudent(int studentId, String name, String email, String course) {
        String sql = "UPDATE students SET name=?, email=?, course=? WHERE student_id=?";
        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, course);
            statement.setInt(4, studentId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Inner class to hold student data
    private class Student {
        private int id;
        private String name;
        private String email;
        private String course;

        public Student(int id, String name, String email, String course) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.course = course;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getCourse() { return course; }
    }
}