import java.sql.*;

public class ConnectDB {
    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:SqliteJavaDB.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite Database has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return conn;
    }
    public void createNewTable() {
        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE employees (
                 id integer PRIMARY KEY,
                 name  text NOT NULL,
                 capacity real
                );""";
        Connection conn;
        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("table created");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTable(String tableName){
        Connection conn;
        // SQL statement for creating a new table
        String sql = "DROP TABLE "+ tableName;

        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println(tableName+" table deleted");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addColumn(String tableName, String columnName){
        Connection conn;
        // SQL statement for creating a new table
        String sql = "ALTER TABLE "+ tableName+ " ADD COLUMN "+ columnName;

        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Added column "+columnName+" to "+tableName+" table");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void renameTable(String oldTableName, String newTableName){
        Connection conn;
        // SQL statement for creating a new table
        String sql = "ALTER TABLE "+ oldTableName + " RENAME TO "+ newTableName;

        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println(oldTableName+" table renamed to "+ newTableName);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String name, double capacity) {
        String sql = "INSERT INTO employees(name, capacity) VALUES(?,?)";
        Connection conn = null;
        try{
            conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
            System.out.println("data entered");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public void selectAll(){
        String sql = "SELECT * FROM employees";

        try {
            Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateTable(String tableName, String columnName, String setValue, String whereCondition){
        Connection conn;
        // SQL statement for creating a new table
        String sql = "UPDATE "+tableName+" set "+columnName+" = "+setValue+" where "+whereCondition+";";

        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println(tableName+" table updated");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTuple(String tableName, String whereCondition){
        Connection conn;
        // SQL statement for creating a new table
        String sql = "DELETE FROM "+ tableName+" WHERE "+whereCondition;

        try{
            conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("tuple deleted from "+tableName+" table");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        ConnectDB app = new ConnectDB();
        app.createNewTable();
        app.insert("Aryan", 30000);
        app.insert("Robert", 40000);
        app.insert("Jerry", 50000);
        app.selectAll();
        app.updateTable("employees", "capacity", "0", "id = 7");
        app.selectAll();
        app.deleteTuple("employees", "id = 10");
        app.selectAll();

    }

}  