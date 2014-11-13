package data;

import util.Config;

import java.sql.*;
import java.util.Properties;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the actual MySQL Database.
 */
public class MySQLConnection {


    private Connection mConnection;

    public static final String DEBUG_TAG = MySQLConnection.class.getSimpleName();
    public static final boolean D = false;

    public boolean open(){
        Properties connectionProps = new Properties();
        connectionProps.put("user", Config.get().getString("database.user"));
        connectionProps.put("password", Config.get().getString("database.password"));

        /*
        String url = Config.MYSQL_HOST + Config.MYSQL_DB_NAME
                + "?user=" + Config.MYSQL_USER
                + "?password=" + Config.MYSQL_PASSWORD;

        Log.add(DEBUG_TAG,"MySQLConnection with: " + url);
        */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //mConnection = DriverManager.getConnection(url);
            mConnection = DriverManager.getConnection(
                    "jdbc:mysql://" +
                            Config.get().getString("database.host")
                            + ":"
                            + Config.get().getString("database.port")
                            + "/",
                    connectionProps);

        } catch(ClassNotFoundException e) {
            Log.add(DEBUG_TAG,"Could not open database: " + e);
            return false;

        } catch(SQLException e) {
            Log.add(DEBUG_TAG,"Could not open database: " + e);
            return false;
        }
        return true;
    }

    public void createDatabase(){
        if(D){Log.add(DEBUG_TAG,"Creating database");}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS "
                    + Config.get().getString("database.db"));
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not create database: " + e);
        }
    }

    public void selectDatabase(){
        if(D){Log.add(DEBUG_TAG,"selecting database");}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "USE " + Config.get().getString("database.db"));
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not select database: " + e);
        }
    }

    public void createTable(String name){
        if(D){Log.add(DEBUG_TAG,"Creating table");}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS " + name + "( temp INT )");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not create table: " + e);
        }
    }

    public void close(){
        if(D){Log.add(DEBUG_TAG,"Closing connection");}
        try {
            mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTemp(int temp){
        if(D){Log.add(DEBUG_TAG,"Inserting temperature data");}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "INSERT INTO " + "test" + " (temp) VALUES (1.4),(2.5),(3.6)");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "write test data: " + e);
        }
    }

    public void test() {
        if(D){Log.add(DEBUG_TAG,"Testing connection!");}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "INSERT INTO " + "test" + " (temp) VALUES (1.4),(2.5),(3.6)");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "write test data: " + e);
        }
    }
}
