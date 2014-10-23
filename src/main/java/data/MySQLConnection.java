package data;

import java.sql.*;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the actual MySQL Database
 */
public class MySQLConnection {

    private String host = "jdbc:mysql://localhost:3306/";
    private String dbName = "testSerial";
    private String username = "root";
    private String password = "";
    private Connection conn;


    public static final String DEBUG_TAG = "MySQLConnection";

    public void open(){
        String url = host + dbName
                + "?user=" + username
                + "?password=" + password;
        Log.add(DEBUG_TAG,"MySQLConn with: " + url);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url);
        }
        catch(ClassNotFoundException e) {
            Log.add(DEBUG_TAG,"Class not found!");
        }
        catch(SQLException e) {
            Log.add(DEBUG_TAG,"SQL Exception!");
        }

    }

    public void close(){
        Log.add(DEBUG_TAG,"Closing connection");
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        Log.add(DEBUG_TAG,"Testing connection!");
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(
                    "INSERT INTO testtable (temp) VALUES (1.4),(2.5),(3.6)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
