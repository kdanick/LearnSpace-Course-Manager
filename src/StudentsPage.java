import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;
import Round.*;

public class StudentsPage extends JPanel {
    private JTable StudentsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterDropdown;

    public StudentsPage() {
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new BorderLayout());


        JLabel titleLabel = new JLabel("Students", SwingConstants.LEFT);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        topPanel.add(titleLabel, BorderLayout.WEST);


        String[] filterOptions = {"All Students", "Enrolled", "Unenrolled"};
        filterDropdown = new JComboBox<>(filterOptions);
        filterDropdown.setPreferredSize(new Dimension(150, 30));
        filterDropdown.setMaximumSize(new Dimension(150, 30));
        filterDropdown.addActionListener(this::filterStudents);
        topPanel.add(filterDropdown, BorderLayout.EAST);

        topPanel.setBorder(new EmptyBorder(10,10,10,10));

        add(topPanel, BorderLayout.NORTH);


        tableModel = new DefaultTableModel();
        StudentsTable = new JTable(tableModel);
        StudentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        StudentsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(StudentsTable);
        add(scrollPane, BorderLayout.CENTER);


        loadStudents("All Students");

        // Buttons Panel
        JPanel buttonPanel = new JPanel(); // Create panel for buttons
        RoundedButton insertButton = new RoundedButton("Add Student");
        RoundedButton updateButton = new RoundedButton("Edit Student");
        RoundedButton deleteButton = new RoundedButton("Remove Students");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(this::insertStudent);
        updateButton.addActionListener(this::updateStudent);
        deleteButton.addActionListener(this::deleteStudent);
    }


    private void loadStudents(String filter) {
        try (Connection connection = Db_connect.getConnection();
             Statement statement = connection.createStatement()) {


            String sqlQuery = "SELECT student_id, name, email, gender FROM Students";
            if ("Enrolled".equals(filter)) {
                sqlQuery += " WHERE student_id IN (SELECT student_id FROM Enrollments)";
            } else if ("Unenrolled".equals(filter)) {
                sqlQuery += " WHERE student_id NOT IN (SELECT student_id FROM Enrollments)";
            }

            ResultSet resultSet = statement.executeQuery(sqlQuery);


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


    private void filterStudents(ActionEvent e) {
        String selectedFilter = (String) filterDropdown.getSelectedItem();
        loadStudents(selectedFilter);
    }


    private void insertStudent(ActionEvent e) {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField genderField = new JTextField();


        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Email:", emailField,
                "Gender: ", genderField
        };


        int option = JOptionPane.showConfirmDialog(this, message, "Enter Student Details", JOptionPane.OK_CANCEL_OPTION);


        if (option == JOptionPane.OK_OPTION) {
            String idText = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String gender = genderField.getText().trim();


            if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idText);


                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO Students (student_id, name, email, gender) VALUES (?, ?, ?, ?)")) {


                    statement.setInt(1, id);
                    statement.setString(2, name);
                    statement.setString(3, email);
                    statement.setString(4, gender);

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Student added successfully.");
                    loadStudents("All Students");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting Student: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    private void updateStudent(ActionEvent e) {
        int selectedRow = StudentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }


        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentGender = (String) tableModel.getValueAt(selectedRow, 3);

        // Create input fields with current values for editing
        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField genderField = new JTextField(currentGender);

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Gender: ", genderField
        };


        int option = JOptionPane.showConfirmDialog(this, message, "Update Student Details", JOptionPane.OK_CANCEL_OPTION);


        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newGender = genderField.getText().trim();


            if (newName.isEmpty() || newEmail.isEmpty() || newGender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE Students SET name=?, email=?, gender=? WHERE student_id=?")) {


                statement.setString(1, newName);
                statement.setString(2, newEmail);
                statement.setString(3, newGender);
                statement.setInt(4, studentId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                loadStudents("All Students");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating Student.");
                ex.printStackTrace();
            }
        }
    }

    // Method to delete a selected student
    private void deleteStudent(ActionEvent e) {
        int selectedRow = StudentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);


        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove student " + studentName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);


        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Students WHERE student_id=?")) {

                statement.setInt(1, studentId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student removed successfully.");
                loadStudents("All Students");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting Student.");
                ex.printStackTrace();
            }
        }
    }
}