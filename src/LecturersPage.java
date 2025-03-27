import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import DatabaseManager.Db_connect;
import Round.RoundedButton;

public class LecturersPage extends JPanel {
    private JTable lecturerTable; // Table to display lecturers
    private DefaultTableModel tableModel;

    public LecturersPage() {
        setLayout(new BorderLayout()); // Set layout manager

        // Top Panel with Title
        JPanel topPanel = new JPanel(new BorderLayout()); // Create a panel for the title

        // Create and configure the title label
        JLabel titleLabel = new JLabel("Lecturers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Set font style
        titleLabel.setForeground(Color.BLACK); // Set text color
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Add padding
        topPanel.add(titleLabel, BorderLayout.NORTH); // Add title label to the top panel
        add(topPanel, BorderLayout.NORTH); // Add top panel to the main panel

        // Table Model
        tableModel = new DefaultTableModel(); // Initialize table model
        lecturerTable = new JTable(tableModel); // Create table with the model
        lecturerTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for table
        lecturerTable.setRowHeight(25); // Set row height for better readability
        JScrollPane scrollPane = new JScrollPane(lecturerTable); // Add table to scroll pane
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center of the main panel

        // Load Lecturers from database
        loadLecturers();

        // Buttons Panel
        JPanel buttonPanel = new JPanel(); // Create panel for buttons
        RoundedButton insertButton = new RoundedButton("Add Lecturer"); // Button to add a lecturer
        RoundedButton updateButton = new RoundedButton("Edit Lecturer"); // Button to edit selected lecturer
        RoundedButton deleteButton = new RoundedButton("Remove Lecturer"); // Button to delete selected lecturer

        // Add buttons to the button panel
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the main panel

        // Button Actions
        insertButton.addActionListener(this::insertLecturer); // Action for inserting a lecturer
        updateButton.addActionListener(this::updateLecturer); // Action for updating a lecturer
        deleteButton.addActionListener(this::deleteLecturer); // Action for deleting a lecturer
    }

    // Method to load lecturers from the database and display them in the table
    private void loadLecturers() {
        try (Connection connection = Db_connect.getConnection(); // Establish database connection
             Statement statement = connection.createStatement(); // Create statement for executing queries
             ResultSet resultSet = statement.executeQuery("SELECT user_id, name, email, phone_no, gender FROM Users WHERE role='lecturer'")) {

            // Get column names from the result set
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount]; // Array to hold column names

            // Populate column names
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
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
            JOptionPane.showMessageDialog(this, "Error loading lecturer data.");
            e.printStackTrace();
        }
    }

    // Method to insert a new lecturer
    private void insertLecturer(ActionEvent e) {
        // Create input fields for lecturer details
        JTextField idField = new JTextField();  // User enters ID
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField genderField = new JTextField();

        // Prepare message for the input dialog
        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Email:", emailField,
                "Phone No:", phoneField,
                "Gender: ", genderField
        };

        // Show dialog for input
        int option = JOptionPane.showConfirmDialog(this, message, "Enter Lecturer Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String idText = idField.getText().trim(); // Get ID input
            String name = nameField.getText().trim(); // Get name input
            String email = emailField.getText().trim(); // Get email input
            String phoneNo = phoneField.getText().trim(); // Get phone number input
            String gender = genderField.getText().trim(); // Get gender input
            String password = "123456"; // Default password

            // Validate input fields
            if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit if validation fails
            }

            try {
                int id = Integer.parseInt(idText);  // Convert ID to integer

                // Insert lecturer into the database
                try (Connection connection = Db_connect.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO Users (user_id, name, email, password_hash, phone_no, gender, role) VALUES (?, ?, ?, ?, ?, ?, 'lecturer')")) {
                    // Set parameters for the prepared statement
                    statement.setInt(1, id);
                    statement.setString(2, name);
                    statement.setString(3, email);
                    statement.setString(4, password);
                    statement.setString(5, phoneNo);
                    statement.setString(6, gender);

                    statement.executeUpdate(); // Execute the insert
                    JOptionPane.showMessageDialog(this, "Lecturer added successfully."); // Success message
                    loadLecturers(); // Refresh the table
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE); // Error for invalid ID
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error inserting lecturer: " + ex.getMessage());
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    // Method to update selected lecturer's details
    private void updateLecturer(ActionEvent e) {
        int selectedRow = lecturerTable.getSelectedRow(); // Get the selected row
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lecturer to update."); // Error if no row selected
            return;
        }

        // Get current details of the selected lecturer
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);
        String currentGender = (String) tableModel.getValueAt(selectedRow, 4);

        // Create input fields with current values for editing
        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField genderField = new JTextField(currentGender);

        // Prepare message for the input dialog
        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone No:", phoneField,
                "Gender: ", genderField
        };

        // Show dialog for updating lecturer details
        int option = JOptionPane.showConfirmDialog(this, message, "Update Lecturer Details", JOptionPane.OK_CANCEL_OPTION);

        // If the user clicks OK, process the input
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String newGender = genderField.getText().trim();

            // Validate input fields
            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newGender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit if validation fails
            }

            // Update lecturer details in the database
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE Users SET name=?, email=?, phone_no=?, gender=? WHERE user_id=? AND role='lecturer'")) {

                // Set parameters for the prepared statement
                statement.setString(1, newName);
                statement.setString(2, newEmail);
                statement.setString(3, newPhone);
                statement.setString(4, newGender);
                statement.setInt(5, userId);
                statement.executeUpdate(); // Execute the update
                JOptionPane.showMessageDialog(this, "Lecturer updated successfully."); // Success message
                loadLecturers(); // Refresh the table
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating lecturer."); // Error message
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    // Method to delete a selected lecturer
    private void deleteLecturer(ActionEvent e) {
        int selectedRow = lecturerTable.getSelectedRow(); // Get the selected row
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lecturer to delete."); // Error if no row selected
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0); // Get user ID for deletion
        String lecturerName = (String) tableModel.getValueAt(selectedRow, 1); // Get lecturer name for confirmation

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove lecturer " + lecturerName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        // If the user confirms, proceed with deletion
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = Db_connect.getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Users WHERE user_id=? AND role='lecturer'")) {

                statement.setInt(1, userId); // Set parameter for deletion
                statement.executeUpdate(); // Execute the delete
                JOptionPane.showMessageDialog(this, "Lecturer removed successfully."); // Success message
                loadLecturers(); // Refresh the table
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting lecturer."); // Error message
                ex.printStackTrace(); // Print stack trace for debugging
            }
        }
    }
}