package data;

import util.Config;

import java.sql.*;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the actual MySQL Database.
 */
public class MySQLConnection {


    private Connection conn;

    public static final String DEBUG_TAG = MySQLConnection.class.getSimpleName();

    public void open(){
        String url = Config.MYSQL_HOST + Config.MYSQL_DBNAME
                + "?user=" + Config.MYSQL_USER
                + "?password=" + Config.MYSQL_PASSWORD;
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
