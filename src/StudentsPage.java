import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;

public class StudentsPage extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton insertButton, updateButton, deleteButton;
    private JTextField nameField, emailField, gradesField;

    public StudentsPage() {
        setLayout(new BorderLayout());

        // Table Model
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load Students
        loadStudents();

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4));
        nameField = new JTextField();
        emailField = new JTextField();
        gradesField = new JTextField();
        insertButton = new JButton("Insert");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Grades:"));
        formPanel.add(gradesField);
        formPanel.add(insertButton);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(this::insertStudent);
        updateButton.addActionListener(this::updateStudent);
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

    private void insertStudent(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        String grades = gradesField.getText();

        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students (name, email, grades) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, grades);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error inserting student.");
            ex.printStackTrace();
        }
    }

    private void updateStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String email = emailField.getText();
        String grades = gradesField.getText();

        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE students SET name=?, email=?, grades=? WHERE student_id=?")) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, grades);
            statement.setInt(4, studentId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating student.");
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

        try (Connection connection = Db_connect.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE student_id=?")) {

            statement.setInt(1, studentId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student.");
            ex.printStackTrace();
        }
    }
}
