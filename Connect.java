import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connect {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:SqliteJavaDB.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite Database has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch ( Exception e ) {

            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

            System.exit(0);

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
    public static void createNewTable() {
        String url = "jdbc:sqlite:SqliteJavaDB.db";

        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE IF NOT EXISTS clothname (
                 id integer PRIMARY KEY,
                 measure_one  text NOT NULL,
                 measure_two text
                );""";

        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void insert(String m1, String m2) {
        String sql = "INSERT INTO clothname (measure_one , measure_two) VALUES(?,?)";
        String url = "jdbc:sqlite:SqliteJavaDB.db";
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, m1);
            pstmt.setDouble(2, m2);
            pstmt.executeUpdate();
            System.out.println("data added :"+ m1+" "+m2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void selectAll(){
        String sql = "SELECT * FROM clothname";
        String url = "jdbc:sqlite:SqliteJavaDB.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columns = new ArrayList<>();
            for(int i = 1; i <= rsmd.getColumnCount();i++){
                System.out.println("col "+i+":"+rsmd.getColumnName(i));
                columns.add(rsmd.getColumnLabel(i));
                System.out.println(columns.get(i-1));
                System.out.println(rsmd.getColumnTypeName(i));
                System.out.println(rsmd.getColumnType(i));
            }
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("measure_one") + "\t" +
                        rs.getDouble("measure_two"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        connect();
        createNewTable();
        insert("34.5", "12.7");
        selectAll();
    }
}