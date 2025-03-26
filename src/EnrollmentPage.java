import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EnrollmentPage extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton insertButton, updateButton, deleteButton;

    public EnrollmentPage() {
        setLayout(new BorderLayout());

        // Table Model
        tableModel = new DefaultTableModel(new Object[]{"Student ID", "Name", "Email", "Phone"}, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load Sample Students
        loadSampleStudents();

        // Buttons
        JPanel buttonPanel = new JPanel();
        insertButton = new JButton("Add Student");
        updateButton = new JButton("Edit Student");
        deleteButton = new JButton("Remove Student");

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
     * Load sample students into the table.
     */
    private void loadSampleStudents() {
        Object[][] sampleData = {
                {1, "John Doe", "john@example.com", "123-456-7890"},
                {2, "Jane Smith", "jane@example.com", "098-765-4321"},
                {3, "Alice Johnson", "alice@example.com", "555-123-4567"},
        };

        for (Object[] student : sampleData) {
            tableModel.addRow(student);
        }
    }

    /**
     * Insert a new student using a popup form.
     */
    private void insertStudent(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enter Student Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add new student with a new ID
            int studentId = tableModel.getRowCount() + 1; // Simple ID generation
            tableModel.addRow(new Object[]{studentId, name, email, phone});
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        }
    }

    /**
     * Update an existing student's details.
     */
    private void updateStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }

        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentEmail = (String) tableModel.getValueAt(selectedRow, 2);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Student Details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.setValueAt(newName, selectedRow, 1);
            tableModel.setValueAt(newEmail, selectedRow, 2);
            tableModel.setValueAt(newPhone, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
        }
    }

    /**
     * Delete a student from the system.
     */
    private void deleteStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the student " + studentName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Student removed successfully.");
        }
    }
}