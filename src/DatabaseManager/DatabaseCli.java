package DatabaseManager;

import java.sql.*;
import java.util.Scanner;

public class DatabaseCli {

    // Method to list all tables in the database
    public static void listTables() {
        try (Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                System.out.println("❌ Failed to establish database connection.");
                return;
            }
            System.out.println("✅ Database connection successful!");

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            boolean hasTables = false;
            System.out.println("\n📌 Tables in the database:");

            while (tables.next()) {
                hasTables = true;
                System.out.println("- " + tables.getString("TABLE_NAME"));
            }

            if (!hasTables) {
                System.out.println("❌ No tables found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error retrieving tables.");
            e.printStackTrace();
        }
    }

    // Method to display data from a specific table
    public static void displayTableData(String tableName) {
        try (Connection connection = Db_connect.getConnection()) {
            if (connection == null) {
                System.out.println("❌ Failed to establish database connection.");
                return;
            }
            System.out.println("✅ Database connection successful!");

            // Verify if table exists first (prevents SQL injection)
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});

            if (!tables.next()) {
                System.out.println("❌ Table '" + tableName + "' does not exist.");
                return;
            }

            // Use metadata to retrieve column names safely
            String query = "SELECT * FROM " + tableName;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                int columnCount = resultSet.getMetaData().getColumnCount();

                System.out.print("\n🔹 Data from table: " + tableName + "\n| ");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getMetaData().getColumnName(i) + " | ");
                }
                System.out.println("\n" + "-".repeat(50));

                boolean hasData = false;
                while (resultSet.next()) {
                    hasData = true;
                    System.out.print("| ");
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getString(i) + " | ");
                    }
                    System.out.println();
                }

                if (!hasData) {
                    System.out.println("❌ No data found in table: " + tableName);
                }

            } catch (SQLException e) {
                System.out.println("⚠️ Error retrieving data from table: " + tableName);
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error connecting to the database.");
            e.printStackTrace();
        }
    }

    // Method to insert data into a given table
    public static void insertData(String tableName, Scanner scanner) {
        try (Connection connection = Db_connect.getConnection();
//             Scanner scanner = new Scanner(System.in)
        ) {

            if (connection == null) {
                System.out.println("❌ Failed to establish database connection.");
                return;
            }
            System.out.println("✅ Database connection successful!");

            // Fetch column details
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder valuesPlaceholder = new StringBuilder(" VALUES (");
            int columnCount = 0;

            while (columns.next()) {
                columnCount++;
                if (columnCount > 1) {
                    query.append(", ");
                    valuesPlaceholder.append(", ");
                }
                query.append(columns.getString("COLUMN_NAME"));
                valuesPlaceholder.append("?");
            }

            query.append(")").append(valuesPlaceholder).append(")");

            if (columnCount == 0) {
                System.out.println("❌ Table not found or has no columns.");
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                columns = metaData.getColumns(null, null, tableName, null);
                int index = 1;

                System.out.println("\n✏️ Enter values for table: " + tableName);
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");

                    System.out.print(columnName + " (" + columnType + "): ");
                    String value = scanner.nextLine();

                    // Set values based on type
                    try {
                        if (columnType.contains("int")) {
                            preparedStatement.setInt(index, Integer.parseInt(value));
                        } else if (columnType.contains("float") || columnType.contains("double")) {
                            preparedStatement.setDouble(index, Double.parseDouble(value));
                        } else if (columnType.contains("bool")) {
                            preparedStatement.setBoolean(index, Boolean.parseBoolean(value));
                        } else {
                            preparedStatement.setString(index, value);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid format for column: " + columnName);
                        return;
                    }
                    index++;
                }

                // Execute insertion
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ Data inserted successfully!");
                } else {
                    System.out.println("❌ Failed to insert data.");
                }
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Error inserting data into table: " + tableName);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter correct values.");
        }
    }

//    updateValues
//    deleteValues

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {

            System.out.println("\nChoose an option:");
            System.out.println("1. View all tables");
            System.out.println("2. View data from a specific table");
            System.out.println("3. Insert data into a table");
            System.out.println("0. END");
            System.out.print("Enter choice: ");

            if (!scanner.hasNextInt()) { // Prevents input mismatch errors
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if(choice == 0) {
                break;
            }
            else if (choice == 1) {
                listTables();
            } else if (choice == 2) {
                System.out.print("Enter table name: ");
                String tableName = scanner.nextLine().trim();
                displayTableData(tableName);
            } else if (choice == 3) {
                System.out.print("Enter table name: ");
                String tableName = scanner.nextLine().trim();
                insertData(tableName, scanner);
            } else {
                System.out.println("❌ Invalid choice.");
            }

        }

        scanner.close();
    }
}
