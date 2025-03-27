import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;
import Round.*;

public class StudentsPage extends JPanel {
    private JTable StudentsTable; // Table to display student data
    private DefaultTableModel tableModel; // Model for the student table

    public StudentsPage() {
        setLayout(new BorderLayout()); // Set layout manager for the main panel

        // Top Panel with Title
        JPanel topPanel = new JPanel(new BorderLayout()); // Create a panel for the title

        // Create and configure the title label
        JLabel titleLabel = new JLabel("Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Set font style
        titleLabel.setForeground(Color.BLACK); // Set text color
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Add padding around the title
        topPanel.add(titleLabel, BorderLayout.NORTH); // Add title label to the top panel
        add(topPanel, BorderLayout.NORTH); // Add top panel to the main panel

        // Table Model
        tableModel = new DefaultTableModel(); // Initialize table model
        StudentsTable = new JTable(tableModel); // Create table with the model
        StudentsTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for table
        StudentsTable.setRowHeight(25); // Set row height for better readability
        JScrollPane scrollPane = new JScrollPane(StudentsTable); // Wrap table in a scroll pane
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center of the main panel

        // Load Students from database
        loadStudents(); // Call method to populate table with student data

        // Buttons Panel
        JPanel buttonPanel = new JPanel(); // Create panel for buttons
        RoundedButton insertButton = new RoundedButton("Add Student"); // Button to add a student
        RoundedButton updateButton = new RoundedButton("Edit Student"); // Button to edit selected student
        RoundedButton deleteButton = new RoundedButton("Remove Students"); // Button to delete selected student

        // Add buttons to the button panel
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the main panel

        // Button Actions
        insertButton.addActionListener(this::insertStudent); // Action for inserting a student
        updateButton.addActionListener(this::updateStudent); // Action for updating a student
        deleteButton.addActionListener(this::deleteStudent); // Action for deleting a student
    }

    // Method to load students from the database and display them in the table
    private void loadStudents() {
        try (Connection connection = Db_connect.getConnection(); // Establish database connection
             Statement statement = connection.createStatement(); // Create statement for executing queries
             ResultSet resultSet = statement.executeQuery("SELECT student_id, name, email, gender FROM Students")) {

            // Get column names from the result set
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); // Get number of columns in the result set
            String[] columnNames = new String[columnCount]; // Array to hold column names

            // Populate column names
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1); // Retrieve each column name
            }

            tableModel.setColumnIdentifiers(columnNames); // Set column identifiers in the table model
            tableModel.setRowCount(0); // Clear previous rows in the table

            // Load data into the table
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount]; // Array to hold row data
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1); // Retrieve data for each column
                }
                tableModel.addRow(rowData); // Add row data to the table model
            }
        } catch (SQLException e) {
            // Show error message if loading fails
            JOptionPane.showMessageDialog(this, "Error loading student data.");
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    // Method to insert a new student
    private void insertStudent(ActionEvent e) {
        // Create input fields for student details
        JTextField idField = new JTextField();  // User enters ID
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField genderField = new JTextField();

        // Prepare message for the input dialog
        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Email:", emailField,
                "Gender: ", genderField
        };

        // Show dialog for input
        int option = JOptionPane.showConfirmDialog(this, message, "Enter Student Details", JOptionPane.OK_CANCEL_OPTION);

        // If the user clicks OK, process the input
        if (option == JOptionPane.OK_OPTION) {
            String idText = idField.getText().trim(); // Get ID input
            String name = nameField.getText().trim(); // Get name input
            String email = emailField.getText().trim(); // Get email input
            String gender = genderField.getText().trim(); // Get gender input

            // Validate input fields
            if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit if validation fails
            }

            try {
                int id = Integer.parseInt(idText);  // Convert ID to integer

                // Insert student into the database
                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO Students (student_id, name, email, gender) VALUES (?, ?, ?, ?)")) {

                    // Set parameters for the prepared statement
                    statement.setInt(1, id);
                    statement.setString(2, name);
                    statement.setString(3, email);
                    statement.setString(4, gender);

                    statement.executeUpdate(); // Execute the insert
                    JOptionPane.showMessageDialog(this, "Student added successfully."); // Success message
                    loadStudents(); // Refresh the table
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE); // Error for invalid ID
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting Student: " + ex.getMessage());
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    // Method to update selected student's details
    private void updateStudent(ActionEvent e) {
        int selectedRow = StudentsTable.getSelectedRow(); // Get the selected row
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update."); // Error if no row selected
            return;
        }

        // Get current details of the selected student
        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentGender = (String) tableModel.getValueAt(selectedRow, 3);

        // Create input fields with current values for editing
        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField genderField = new JTextField(currentGender);

        // Prepare message for the input dialog
        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Gender: ", genderField
        };

        // Show dialog for updating student details
        int option = JOptionPane.showConfirmDialog(this, message, "Update Student Details", JOptionPane.OK_CANCEL_OPTION);

        // If the user clicks OK, process the input
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newGender = genderField.getText().trim();

            // Validate input fields
            if (newName.isEmpty() || newEmail.isEmpty() || newGender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit if validation fails
            }

            // Update student details in the database
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE Students SET name=?, email=?, gender=? WHERE student_id=?")) {

                // Set parameters for the prepared statement
                statement.setString(1, newName);
                statement.setString(2, newEmail);
                statement.setString(3, newGender);
                statement.setInt(4, studentId);
                statement.executeUpdate(); // Execute the update
                JOptionPane.showMessageDialog(this, "Student updated successfully."); // Success message
                loadStudents(); // Refresh the table
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating Student."); // Error message
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    // Method to delete a selected student
    private void deleteStudent(ActionEvent e) {
        int selectedRow = StudentsTable.getSelectedRow(); // Get the selected row
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete."); // Error if no row selected
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0); // Get student ID for deletion
        String studentName = (String) tableModel.getValueAt(selectedRow, 1); // Get student name for confirmation

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove student " + studentName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        // If the user confirms, proceed with deletion
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Students WHERE student_id=?")) {

                statement.setInt(1, studentId); // Set parameter for deletion
                statement.executeUpdate(); // Execute the delete
                JOptionPane.showMessageDialog(this, "Student removed successfully."); // Success message
                loadStudents(); // Refresh the table
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting Student."); // Error message
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }
}