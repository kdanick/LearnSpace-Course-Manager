import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;

public class StudentsPage extends JPanel {
    private JTable StudentsTable;
    private DefaultTableModel tableModel;
    private JButton insertButton, updateButton, deleteButton;

    public StudentsPage() {
        setLayout(new BorderLayout());

        // Top Panel with Title
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        // Table Model
        tableModel = new DefaultTableModel();
        StudentsTable = new JTable(tableModel);
        StudentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        StudentsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(StudentsTable);
        add(scrollPane, BorderLayout.CENTER);


        // Load Students
        loadStudents();

        // Buttons
        JPanel buttonPanel = new JPanel();
        insertButton = new JButton("Add Student");
        updateButton = new JButton("Edit Student");
        deleteButton = new JButton("Remove Students");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(this::insertStudent);
        updateButton.addActionListener(this::updateStudent);
        deleteButton.addActionListener(this::deleteStudent);
    }

    /**
     * Load only students from the database.
     */
    private void loadStudents() {
        try (Connection connection = Db_connect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT student_id, name, email, gender FROM Students")) {

            // Get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            tableModel.setColumnIdentifiers(columnNames);
            tableModel.setRowCount(0);

            // Load data into the table
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

    /**
     * Insert a new Student using a popup form.
     */
    private void insertStudent(ActionEvent e) {
        JTextField idField = new JTextField();  // User enters ID
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField genderField = new JTextField();

        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Email:", emailField,
                "gender: ", genderField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enter student Details", JOptionPane.OK_CANCEL_OPTION);

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
                int id = Integer.parseInt(idText);  // Convert ID to integer

                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO Students (student_id, name, email, gender) VALUES (?, ?, ?, ?)")) {

                    statement.setInt(1, id);
                    statement.setString(2, name);
                    statement.setString(3, email);
                    statement.setString(4, gender);

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Student added successfully.");
                    loadStudents();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting Student: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Update an existing student's details.
     */
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
                statement.setString(3,newGender);
                statement.setInt(4, studentId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                loadStudents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating Student.");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Delete a student from the system.
     */
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
                loadStudents();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting Student.");
                ex.printStackTrace();
            }
        }
    }
}