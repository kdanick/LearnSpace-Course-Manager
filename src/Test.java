import DatabaseManager.Db_connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Test extends JFrame {
    private JComboBox<String> actionComboBox;
    private JTextField tableNameField;
    private JTextArea resultArea;
    private JButton executeButton;

    public Test() {
        setTitle("LearnSpace Database Manager");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dropdown for actions
        String[] actions = {"View All Tables", "View Table Data", "Insert Data"};
        actionComboBox = new JComboBox<>(actions);
        tableNameField = new JTextField(20);
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        executeButton = new JButton("Execute");

        // Top Panel: Action selection
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Action: "));
        topPanel.add(actionComboBox);
        topPanel.add(new JLabel("Table Name: "));
        topPanel.add(tableNameField);
        topPanel.add(executeButton);

        // Center Panel: Results
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAction();
            }
        });
    }

    private void handleAction() {
        String action = (String) actionComboBox.getSelectedItem();
        String tableName = tableNameField.getText().trim();

        switch (action) {
            case "View All Tables" -> listTables();
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
        }
    }

    private void listTables() {
        try (Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                resultArea.setText("❌ Failed to establish database connection.");
                return;
            }

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            StringBuilder result = new StringBuilder("📌 Tables in the database:\n");

            boolean hasTables = false;
            while (tables.next()) {
                hasTables = true;
                result.append("- ").append(tables.getString("TABLE_NAME")).append("\n");
            }

            if (!hasTables) {
                resultArea.setText("❌ No tables found in the database.");
            } else {
                resultArea.setText(result.toString());
            }
        } catch (SQLException e) {
            resultArea.setText("⚠️ Error retrieving tables.");
            e.printStackTrace();
        }
    }

    private void displayTableData(String tableName) {
        try (Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                resultArea.setText("❌ Failed to establish database connection.");
                return;
            }

            String query = "SELECT * FROM " + tableName;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                int columnCount = resultSet.getMetaData().getColumnCount();
                StringBuilder result = new StringBuilder("🔹 Data from table: " + tableName + "\n");

                for (int i = 1; i <= columnCount; i++) {
                    result.append(resultSet.getMetaData().getColumnName(i)).append(" | ");
                }
                result.append("\n").append("-".repeat(50)).append("\n");

                boolean hasData = false;
                while (resultSet.next()) {
                    hasData = true;
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(resultSet.getString(i)).append(" | ");
                    }
                    result.append("\n");
                }

                if (!hasData) {
                    result.append("❌ No data found in table: ").append(tableName);
                }

                resultArea.setText(result.toString());

            } catch (SQLException e) {
                resultArea.setText("⚠️ Error retrieving data from table: " + tableName);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            resultArea.setText("⚠️ Error connecting to the database.");
            e.printStackTrace();
        }
    }

    private void insertData(String tableName) {
        try (Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                resultArea.setText("❌ Failed to establish database connection.");
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
                resultArea.setText("❌ No columns found in table: " + tableName);
                return;
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
                    resultArea.setText("✅ Data inserted successfully into " + tableName);
                }
            }
        }
        catch (NumberFormatException e) {
            resultArea.setText("❌ Invalid input type. Ensure numbers are entered correctly.");
        } catch (SQLException e) {
            resultArea.setText("⚠️ Error inserting data into table: " + tableName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Test gui = new Test();
            gui.setVisible(true);
        });
    }
}
