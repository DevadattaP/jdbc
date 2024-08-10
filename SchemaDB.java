import java.sql.*;
import java.util.Map;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class SchemaDB {
    // Defined Hashtable for table:{column: datatype}
    public static Hashtable<String, Hashtable<String, String>> tables = new Hashtable<>();

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return Connection object representing the database connection.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // Load the JDBC driver for SQLite
            Class.forName("org.sqlite.JDBC");

            // Define the database URL
            String url = "jdbc:sqlite:Testing.db";

            // Establish connection to the database
            conn = DriverManager.getConnection(url);

            // Print connection establishment confirmation
            System.out.println("Connection to SQLite Database has been established.\n");
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println(e.getMessage());
        } catch (Exception e) {
            // Handle general exceptions
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return conn;
    }

    /**
     * Checks if a table exists in the database.
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table to check for existence.
     * @return          True if the table exists, false otherwise.
     */
    public static boolean tableExists(Connection conn, String tableName) {
        try {
            // Get database metadata to retrieve table information
            DatabaseMetaData meta = conn.getMetaData();

            // Retrieve information about tables matching the specified name
            ResultSet tables = meta.getTables(null, null, tableName, null);

            // Check if the result set contains any rows (i.e., if the table exists)
            boolean exists = tables.next();

            // Close the result set to release resources
            tables.close();

            // Print a message if the table does not exist
            if (!exists) {
                System.out.println(tableName + " table does not exist");
            }

            // Return whether the table exists or not
            return exists;
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false; // Return false in case of an exception
        }
    }

    /**
     * Checks if a table contains any entries (rows).
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table to check for entries.
     * @return          True if the table has entries, false otherwise.
     */
    public static boolean tableFilled(Connection conn, String tableName) {
        try {
            // Create a statement for executing SQL queries
            Statement stmt = conn.createStatement();

            // Execute a query to retrieve the first row from the table
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");

            // Check if the result set contains any rows (i.e., if the table is filled)
            boolean exists = rs.next();

            // Close the statement and result set to release resources
            stmt.close();
            rs.close();

            // Print a message if the table does not have any entries
            if (!exists) {
                System.out.println(tableName + " does not have any entry");
            }

            // Return whether the table has entries or not
            return exists;
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false; // Return false in case of an exception
        }
    }

    /**
     * Creates a new table in the database if it does not already exist.
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table to be created.
     * @param columns   Hashtable containing column names and their corresponding data types.
     */
    public static void createTable(Connection conn, String tableName, Hashtable<String, String> columns) {
        if (!tableExists(conn, tableName)) {
            // Construct SQL statement to create the table
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " ( ");
            Enumeration<String> cols = columns.keys();
            while (cols.hasMoreElements()) {
                String col_name = cols.nextElement();
                sql.append(col_name).append(" ").append(columns.get(col_name)).append(", ");
            }

            // Remove the trailing comma and space
            sql.setLength(sql.length() - 2);
            sql.append(")");

            try {
                // Create the table in the database
                Statement stmt = conn.createStatement();
                stmt.execute(sql.toString());
                System.out.println(tableName + " table created");
                stmt.close();

                // Add table metadata to the HashMap
                tables.put(tableName, columns);
                System.out.println("Table added to the HashMap");

                // Update table metadata
                updateTableMetadata(conn, tableName);

                System.out.println(tables.get(tableName)); // Print table metadata
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(tableName + " table already exists.");
        }
    }

    /**
     * Deletes a table from the database and updates the Hashtable.
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table to be deleted.
     */
    public static void deleteTable(Connection conn, String tableName) {
        if (tableExists(conn, tableName)) {
            String sql = "DROP TABLE IF EXISTS " + tableName;
            try {
                // Execute SQL to drop the table
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();

                // Remove the table entry from the Hashtable
                tables.remove(tableName);
                System.out.println(tableName + " table deleted from the database and Hashtable updated.");
            } catch (SQLException e) {
                // Handle SQL exceptions
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Selects specified columns from a specified table in the database and prints them.
     * If no column names are provided, selects all columns.
     *
     * @param conn         Connection object representing the database connection.
     * @param tableName    Name of the table from which to select entries.
     * @param columnNames  Optional: List of column names to select. If null or empty, selects all columns.
     */
    public static void selectColumns(Connection conn, String tableName, List<String> columnNames) {
        if (tableExists(conn, tableName)) {
            if (tableFilled(conn, tableName)) {
                // Construct SQL query to select specified columns or all columns
                StringBuilder sql = new StringBuilder("SELECT ");
                if (columnNames == null || columnNames.isEmpty()) {
                    sql.append("*"); // Select all columns
                } else {
                    // Select specified columns
                    for (String columnName : columnNames) {
                        sql.append(columnName).append(", ");
                    }
                    // Remove the trailing comma and space
                    sql.setLength(sql.length() - 2);
                }
                sql.append(" FROM ").append(tableName);

                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql.toString());

                    // Retrieve column names if selecting all columns
                    if (columnNames == null || columnNames.isEmpty()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        columnNames = new ArrayList<>();
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames.add(metaData.getColumnName(i));
                        }
                    }

                    // Print column names
                    System.out.println("\n" + tableName + " :");
                    for (String columnName : columnNames) {
                        System.out.print(columnName + "\t");
                    }
                    System.out.println(); // New line after printing column names

                    // Loop through the result set
                    while (rs.next()) {
                        // Print each specified column value
                        for (String columnName : columnNames) {
                            String columnValue = rs.getString(columnName);
                            System.out.print(columnValue + "\t\t");
                        }
                        System.out.println(); // New line after printing all columns for a row
                    }
                    stmt.close(); // Close the statement
                    System.out.println(); // Empty line for better output readability
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Inserts values into the specified table in the database.
     * if it is the "cloths" table, it will also create a new table with the name provided in the "cloth_name" value.
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table to insert values into.
     * @param values    Hashtable containing column names and their corresponding values to be inserted.
     */
    public static void insertValues(Connection conn, String tableName, Hashtable<String, Object> values) {
        // Check if the table exists
        if (tableExists(conn, tableName)) {
            // Construct the SQL INSERT statement
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder placeholders = new StringBuilder("VALUES (");
            Enumeration<String> keys = values.keys();

            // Build the column names and placeholders for values dynamically
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                sql.append(key).append(",");
                placeholders.append("?,");
            }
            // Remove the trailing comma
            sql.setLength(sql.length() - 1);
            placeholders.setLength(placeholders.length() - 1);

            // Closing parentheses for the SQL statement
            sql.append(") ");
            placeholders.append(")");

            // Combine SQL statement and placeholders
            sql.append(placeholders);

            try {
                // Create a prepared statement
                PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                int index = 1;
                Enumeration<Object> elements = values.elements();
                // Bind values to placeholders
                while (elements.hasMoreElements()) {
                    Object value = elements.nextElement();
                    pstmt.setObject(index, value);
                    index++;
                }
                // Execute the SQL INSERT statement
                pstmt.executeUpdate();
                System.out.println("Values inserted into " + tableName + " table.");
                pstmt.close(); // Close the prepared statement

                // Check if the table is "cloths" to create a new table with cloth_name
                try {
                    if (tableName.equals("cloths")) {
                        // Get the value of "cloth_name"
                        String clothName = values.get("cloth_name").toString();
                        // Create a new table with the cloth_name as table name
                        createTable(conn, clothName, tables.get(clothName));
                    }
                } catch (Exception ex) {
                    // Print any exceptions that occur during table creation
                    System.out.println(ex.getMessage());
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage()); // Handle SQL exceptions
            }
        } else {
            System.out.println(tableName + " table does not exist.");
        }
    }

    /**
     * Adds a new column to the specified table in the database.
     *
     * @param conn        Connection object representing the database connection.
     * @param tableName   Name of the table to add the column to.
     * @param columnName  Name of the new column to add.
     * @param columnType  Data type of the new column.
     */
    public static void addColumn(Connection conn, String tableName, String columnName, String columnType) {
        // Check if the table exists
        if (tableExists(conn, tableName)) {
            // Construct the SQL ALTER TABLE statement to add the column
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
            try {
                // Execute the SQL statement
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println("Added column " + columnName + " to " + tableName + " table");
                stmt.close(); // Close the statement

                // Update Hashtable with the new column
                if (tables.containsKey(tableName)) {
                    Hashtable<String, String> columns = tables.get(tableName);
                    columns.put(columnName, columnType);
                    tables.put(tableName, columns);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage()); // Handle SQL exceptions
            }
        }
    }

    /**
     * Drops a column from the specified table in the database.
     *
     * @param conn       Connection object representing the database connection.
     * @param tableName  Name of the table from which to drop the column.
     * @param columnName Name of the column to be dropped.
     */
    public static void dropColumn(Connection conn, String tableName, String columnName) {
        try {
            // Create a temporary table name
            String tempTableName = tableName + "_temp";

            // Create temporary table with desired columns
            String createTempTableSQL = "CREATE TABLE " + tempTableName + " AS SELECT * FROM " + tableName;
            Statement stmt = conn.createStatement();
            stmt.execute(createTempTableSQL);

            // Drop original table
            String dropOriginalTableSQL = "DROP TABLE " + tableName;
            stmt.execute(dropOriginalTableSQL);

            // Recreate original table without the dropped column
            // Assuming the rest of the columns are already defined in the Hashtable
            StringBuilder recreateTableSQL = new StringBuilder("CREATE TABLE " + tableName + " AS SELECT ");
            Hashtable<String, String> columns = tables.get(tableName);

            Iterator<Map.Entry<String, String>> iterator = columns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String col = entry.getKey();
                if (!col.equals(columnName)) {
                    recreateTableSQL.append(col).append(", ");
                } else {
                    // Remove the column from the Hashtable
                    iterator.remove();
                }
            }
            // Remove the trailing comma and space
            recreateTableSQL = new StringBuilder(recreateTableSQL.substring(0, recreateTableSQL.length() - 2));
            recreateTableSQL.append(" FROM ").append(tempTableName);
            stmt.execute(recreateTableSQL.toString());

            // Drop the temporary table
            String dropTempTableSQL = "DROP TABLE " + tempTableName;
            stmt.execute(dropTempTableSQL);

            System.out.println("Dropped column " + columnName + " from table " + tableName);

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Handle SQL exceptions
        }
    }

    /**
     * Updates values in a specified column of the specified table in the database based on a given condition.
     *
     * @param conn           Connection object representing the database connection.
     * @param tableName      Name of the table to update.
     * @param columnName     Name of the column to update.
     * @param setValue       New value to set in the specified column.
     * @param whereCondition Condition to filter the rows to be updated.
     */
    public static void updateTable(Connection conn, String tableName, String columnName, String setValue, String whereCondition) {
        // Check if the table exists
        if (tableExists(conn, tableName)) {
            // Check if the table is filled with data
            if (tableFilled(conn, tableName)) {
                // Construct the SQL UPDATE statement
                String sql = "UPDATE " + tableName + " SET " + columnName + " = " + setValue + " WHERE " + whereCondition;
                System.out.println(sql); // Print the generated SQL statement for debugging

                try {
                    // Execute the SQL UPDATE statement
                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);
                    System.out.println(tableName + " table updated");
                    stmt.close(); // Close the statement
                } catch (SQLException e) {
                    System.out.println(e.getMessage()); // Handle SQL exceptions
                }
            }
        }
    }

    /**
     * Deletes tuple(s) from the specified table in the database based on a given condition.
     *
     * @param conn           Connection object representing the database connection.
     * @param tableName      Name of the table from which to delete tuple(s).
     * @param whereCondition Condition to filter the tuple(s) to be deleted.
     */
    public static void deleteTuple(Connection conn, String tableName, String whereCondition) {
        // Check if the table exists
        if (tableExists(conn, tableName)) {
            // Check if the table is filled with data
            if (tableFilled(conn, tableName)) {
                // Construct the SQL DELETE statement
                String sql = "DELETE FROM " + tableName + " WHERE " + whereCondition;

                try {
                    // Execute the SQL DELETE statement
                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);
                    System.out.println("Tuple(s) deleted from " + tableName + " table");
                    stmt.close(); // Close the statement
                } catch (SQLException e) {
                    System.out.println(e.getMessage()); // Handle SQL exceptions
                }
            }
        }
    }

    /**
     * Renames a table in the database.
     *
     * @param conn         Connection object representing the database connection.
     * @param oldTableName Name of the table to be renamed.
     * @param newTableName New name for the table.
     */
    public static void renameTable(Connection conn, String oldTableName, String newTableName) {
        // Check if the old table exists
        if (tableExists(conn, oldTableName)) {
            String sql = "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName;
            try {
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println(oldTableName + " table renamed to " + newTableName);
                stmt.close();

                // Update Hashtable entry
                if (tables.containsKey(oldTableName)) {
                    // Remove the old entry and add a new one with the updated table name
                    Hashtable<String, String> columns = tables.remove(oldTableName);
                    tables.put(newTableName, columns);
                    System.out.println("Hashtable entry updated: " + oldTableName + " renamed to " + newTableName);
                } else {
                    // If there's no Hashtable entry for the old table, update metadata for the new table
                    System.out.println("No Hashtable entry found for table " + oldTableName);
                    updateTableMetadata(conn, newTableName);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage()); // Handle SQL exceptions
            }
        } else {
            System.out.println(oldTableName + " table does not exist");
        }
    }

    /**
     * Renames a column in the specified table of the database.
     *
     * @param conn          Connection object representing the database connection.
     * @param tableName     Name of the table containing the column to be renamed.
     * @param oldColumnName Name of the column to be renamed.
     * @param newColumnName New name for the column.
     */
    public static void renameColumn(Connection conn, String tableName, String oldColumnName, String newColumnName) {
        try {
            // Create a temporary table name
            String tempTableName = tableName + "_temp";

            // Update table metadata to ensure the column information is up-to-date
            updateTableMetadata(conn, tableName);

            // Create temporary table with the new column name and copy data
            StringBuilder createTempTableSQL = new StringBuilder("CREATE TABLE " + tempTableName + " AS SELECT ");
            Hashtable<String, String> columns = tables.get(tableName);
            for (String col : columns.keySet()) {
                if (!col.equals(oldColumnName)) {
                    createTempTableSQL.append(col).append(", ");
                } else {
                    // Rename the column as part of the SELECT statement
                    createTempTableSQL.append(oldColumnName).append(" AS \"").append(newColumnName).append("\", ");
                }
            }
            // Remove the trailing comma and space
            createTempTableSQL = new StringBuilder(createTempTableSQL.substring(0, createTempTableSQL.length() - 2));
            createTempTableSQL.append(" FROM ").append(tableName);

            Statement stmt = conn.createStatement();
            stmt.execute(createTempTableSQL.toString());

            // Drop the original table
            String dropOriginalTableSQL = "DROP TABLE " + tableName;
            stmt.execute(dropOriginalTableSQL);

            // Rename the temporary table to the original table name
            String renameTableSQL = "ALTER TABLE " + tempTableName + " RENAME TO " + tableName;
            stmt.execute(renameTableSQL);

            System.out.println("Column " + oldColumnName + " renamed to " + newColumnName + " in table " + tableName);

            // Update table metadata after renaming
            updateTableMetadata(conn, tableName);
            System.out.println("Updated metadata for table: " + tableName);
            System.out.println(tables.get(tableName)); // Print updated table metadata

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage()); // Handle SQL exceptions
        }
    }

    /**
     * Closes the database connection.
     *
     * @param conn Connection object representing the database connection to be closed.
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); // Close the connection
                System.out.println("\nDatabase connection closed..."); // Print confirmation message
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage()); // Handle SQL exceptions
        }
    }

    /**
     * Updates the metadata of a table in the tables Hashtable based on the actual columns in the database table.
     *
     * @param conn      Connection object representing the database connection.
     * @param tableName Name of the table whose metadata is to be updated.
     */
    public static void updateTableMetadata(Connection conn, String tableName) {
        try {
            // Retrieve database metadata
            DatabaseMetaData metaData = conn.getMetaData();

            // Retrieve information about the columns in the specified table
            ResultSet rs = metaData.getColumns(null, null, tableName, null);

            // Create a Hashtable to store column names and data types
            Hashtable<String, String> columns = new Hashtable<>();
            while (rs.next()) {
                // Extract column name and data type from the ResultSet
                String columnName = rs.getString("COLUMN_NAME");
                String dataType = rs.getString("TYPE_NAME");

                // Put column name and data type into the Hashtable
                columns.put(columnName, dataType);
            }

            // Update the tables Hashtable with the new column information
            tables.put(tableName, columns);

            // Print confirmation message
            System.out.println("Table metadata updated for table: " + tableName);

            // Close the ResultSet
            rs.close();
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves the maximum value from the specified column in the given table.
     * If the table is empty or an error occurs during execution,
     * returns 0 as the maximum value.
     *
     * @param conn       The Connection object for the database.
     * @param tableName  The name of the table from which to retrieve the maximum value.
     * @param columnName The name of the column from which to retrieve the maximum value.
     * @return The maximum value present in the specified column of the table, or 0 if the table is empty or an error occurs.
     */
    public static int getMaxId(Connection conn, String tableName, String columnName) {
        // Initialize maxId to 0
        int maxId = 0;
        try {
            // Create a statement object
            Statement stmt = conn.createStatement();
            // Execute query to get the maximum ID from the table
            ResultSet rs = stmt.executeQuery("SELECT MAX(cast("+columnName+" as integer)) as max_id FROM " + tableName);
            // Check if the result set has data
            if (rs.next()) {
                // Retrieve the maximum value from the result set
                maxId = rs.getInt("max_id");
            }
            // Close the result set and statement objects
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // Print any SQL exceptions that occur
            System.out.println(e.getMessage());
        }
        // Return the maximum ID
        return maxId;
    }

    /**
     * Creates a Hashtable representing the columns and their data types for the "cloths" table.
     *
     * @return Hashtable containing column names as keys and their corresponding data types as values.
     */
    public static Hashtable<String, String> createClothsTable() {
        Hashtable<String, String> columns = new Hashtable<>();
        columns.put("cloth_id", "INTEGER");
        columns.put("cloth_name", "TEXT");
        return columns;
    }

    /**
     * Creates a Hashtable representing the columns and their data types for the "customer" table.
     *
     * @return Hashtable containing column names as keys and their corresponding data types as values.
     */
    public static Hashtable<String, String> createCustomerTable() {
        Hashtable<String, String> columns = new Hashtable<>();
        columns.put("customer_id", "INTEGER");
        columns.put("customer_name", "TEXT");
        columns.put("phone_no", "INTEGER");
        columns.put("age", "TEXT"); // Assuming age is stored as text, modify if necessary
        return columns;
    }

    /**
     * Creates a Hashtable representing the columns and their data types for the "work" table.
     *
     * @return Hashtable containing column names as keys and their corresponding data types as values.
     */
    public static Hashtable<String, String> createWorkTable() {
        Hashtable<String, String> columns = new Hashtable<>();
        columns.put("work_id", "INTEGER");
        columns.put("customer_id", "INTEGER");
        columns.put("cloth_id", "INTEGER");
        columns.put("input_date", "DATE");
        columns.put("fashion", "TEXT");
        columns.put("exp_op_date", "DATE");
        columns.put("pay_amt", "INTEGER");
        columns.put("work_done", "INTEGER");
        columns.put("paid", "INTEGER");
        columns.put("fin_date", "DATE");
        return columns;
    }

    public static void main(String[] args) {
        // Populate tables Hashtable with predefined tables and columns
        tables.put("cloths", createClothsTable());
        tables.put("customer", createCustomerTable());
        tables.put("work", createWorkTable());

        // Print the contents of the 'tables' Hashtable
        System.out.println(tables);

        // Establish a connection to the SQLite database
        Connection conn = connect();

        // Create the tables in the database using the columns defined in the Hashtable
        createTable(conn, "cloths", tables.get("cloths"));
        createTable(conn, "customer", tables.get("customer"));
        createTable(conn, "work", tables.get("work"));

        // Retrieve and display all columns from the 'cloths' table
        selectColumns(conn, "cloths", null);

//        // Define a list of specific columns to retrieve from the 'work' table
//        List<String> specificColumns = new ArrayList<>();
//        specificColumns.add("pay_amt");
//        specificColumns.add("fin_date");
//        // Retrieve and display specific columns from the 'work' table
//        selectColumns(conn, "work", specificColumns);
//        int max = getMaxId(conn, "cloths");
//        System.out.println(max);
        // Create a Hashtable to store values for inserting into the 'cloths' table
//        Hashtable<String, Object> clothValues = new Hashtable<>();
//        clothValues.put("cloth_id", max+1);
//        clothValues.put("cloth_name", "Cotton");
//        // Insert the values into the 'cloths' table
//        insertValues(conn, "cloths", clothValues);
//        System.out.println(getMaxId(conn, "cloths"));

//        // Update the 'cloth_id' column value to '2' where 'cloth_name' is 'Silk' in the 'cloths' table
//        updateTable(conn, "cloths", "cloth_id", "2", "cloth_name = 'Silk'");
//
//        // Rename the 'cloths' table to 'cloths_temp'
//        renameTable(conn, "cloths", "cloths_temp");
//
//        // Delete the 'cloths_temp' table
//        deleteTable(conn, "cloths_temp");
//
        // Delete a tuple from the 'cloths' table where 'cloth_id' is '1'
//        deleteTuple(conn, "cloths", "cloth_name = \"Cotton\"");
//        System.out.println(getMaxId(conn, "cloths"));
//        // Rename the column 'pay_amt' to 'payable_amt' in the 'work' table
//        renameColumn(conn, "work", "pay_amt","payable_amt");
//
//        // Add a new column 'duration' of type 'date' to the 'work' table
//        addColumn(conn, "work", "duration", "date");
//
//        // Drop the 'duration' column from the 'work' table
//        dropColumn(conn, "work", "duration");

        // Close the connection to the SQLite database
        closeConnection(conn);
    }
}
