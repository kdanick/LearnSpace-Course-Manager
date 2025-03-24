package DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DatabaseGUI extends JFrame {
    private JComboBox<String> actionComboBox;
    private JTextField tableNameField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton executeButton, deleteButton;
    private String lastDisplayedTable; // Stores last viewed table for deletion
    private int selectedRow = -1; // Stores selected row index

    public DatabaseGUI() {
        setTitle("LearnSpace Database Manager");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dropdown for actions
        String[] actions = {"View All Tables", "View Table Data", "Insert Data"};
        actionComboBox = new JComboBox<>(actions);
        tableNameField = new JTextField(20);
        executeButton = new JButton("Execute");
        deleteButton = new JButton("Delete Selected Row");
        deleteButton.setEnabled(false); // Initially disabled

        // Top Panel: Action selection
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Action: "));
        topPanel.add(actionComboBox);
        topPanel.add(new JLabel("Table Name: "));
        topPanel.add(tableNameField);
        topPanel.add(executeButton);

        // Table Model
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Bottom Panel: Delete Row
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        executeButton.addActionListener(e -> handleAction());
        deleteButton.addActionListener(e -> deleteRow());

        resultTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && resultTable.getSelectedRow() != -1) {
                selectedRow = resultTable.getSelectedRow();
                deleteButton.setEnabled(true);
            }
        });
    }

    private void handleAction() {
        String action = (String) actionComboBox.getSelectedItem();
        String tableName = tableNameField.getText().trim();

        switch (action) {
            case "View All Tables" -> {
                listTables();
                deleteButton.setEnabled(false);
            }
            case "View Table Data" -> {
                if (tableName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a table name.");
                    return;
                }
                displayTableData(tableName);
            }
            case "Insert Data" -> {
                if (tableName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a table name.");
                    return;
                }
                insertData(tableName);
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void listTables() {
        try (Connection connection = Db_connect.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Table Name"});
            while (tables.next()) {
                tableModel.addRow(new Object[]{tables.getString("TABLE_NAME")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving tables.");
            e.printStackTrace();
        }
    }

    private void displayTableData(String tableName) {
        try (Connection connection = Db_connect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

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

            lastDisplayedTable = tableName;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving data from table: " + tableName);
            e.printStackTrace();
        }
    }

    private void insertData(String tableName) {
        try(Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(this, "❌ Failed to establish database connection.");
                return;
            }

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder values = new StringBuilder(" VALUES (");
            Object[] inputs = new Object[10];
            int columnCount = 0;
            int index = 0;

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");

                String input = JOptionPane.showInputDialog(this, "Enter value for " + columnName + " (" + columnType + "):");

                if (input == null) return;

                if (columnType.equalsIgnoreCase("int4") || columnType.equalsIgnoreCase("integer")) {
                    inputs[index] = Integer.parseInt(input);
                } else {
                    inputs[index] = input;
                }

                query.append(columnName).append(", ");
                values.append("?, ");

                columnCount++;
                index++;
            }
            if (columnCount == 0) {
                JOptionPane.showMessageDialog(this, "❌ No columns found in table: " + tableName);
            }

            query.setLength(query.length() - 2);
            values.setLength(values.length() - 2);
            query.append(")").append(values).append(")");

            try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < columnCount; i++) {
                    preparedStatement.setObject(i + 1, inputs[i]);
                }

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Data inserted successfully into " + tableName);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Invalid input type. Ensure numbers are entered correctly.");
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error inserting data into table: " + tableName);
            e.printStackTrace();
        }
    }

    private void deleteRow() {
        if (selectedRow == -1 || lastDisplayedTable == null) {
            JOptionPane.showMessageDialog(this, "No row selected for deletion.");
            return;
        }

        String primaryKeyColumn = tableModel.getColumnName(0); // Assuming first column is primary key
        Object primaryKeyValue = tableModel.getValueAt(selectedRow, 0);

        if (primaryKeyValue == null) {
            JOptionPane.showMessageDialog(this, "Invalid row selection.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete row where " + primaryKeyColumn + " = " + primaryKeyValue + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection connection = Db_connect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + lastDisplayedTable + " WHERE " + primaryKeyColumn + " = ?")) {
            preparedStatement.setObject(1, primaryKeyValue);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Row deleted successfully.");
                deleteButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "No matching row found for deletion.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting row from " + lastDisplayedTable);
            e.printStackTrace();
        }
    }

    private static void checkDatabaseConnection() {
        System.out.println("Checking database connection...");
        try(Connection connection = Db_connect.getConnection()) {
            if (connection != null) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("❌ Failed to establish database connection.");
            }
        } catch(SQLException e) {
            System.out.println("⚠️ Error connecting to the Database!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseGUI gui = new DatabaseGUI();
            gui.setVisible(true);
        });

        checkDatabaseConnection();
    }
}
