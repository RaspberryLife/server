package data;

import system.Config;
import util.Log;

import java.sql.*;
import java.util.Properties;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the actual MySQL Database.
 */
public class MySqlConnection {


    private Connection mConnection;

    public static final String DEBUG_TAG = MySqlConnection.class.getSimpleName();
    public static final boolean D = true;

    public boolean open(){
        Log.add(DEBUG_TAG,"Opening database connection", D);
        Properties connectionProps = new Properties();
        connectionProps.put("user", Config.getConf().getString("database.user"));
        connectionProps.put("password", Config.getConf().getString("database.password"));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            mConnection = DriverManager.getConnection(
                    "jdbc:mysql://" +
                            Config.getConf().getString("database.host")
                            + ":"
                            + Config.getConf().getString("database.port")
                            + "/",
                    connectionProps);

        } catch(ClassNotFoundException e) {
            Log.add(DEBUG_TAG, "Could not open database: ", e);
            return false;

        } catch(SQLException e) {
            Log.add(DEBUG_TAG,"Could not open database: ", e);
            return false;
        } catch(Exception e) {
            Log.add(DEBUG_TAG,"Could not open database: ", e);
            return false;
        }
        return true;
    }

    public void createDatabase(){
        Log.add(DEBUG_TAG,"Creating database", D);
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS "
                    + Config.getConf().getString("database.db"));
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not create database: ", e);
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Could not create database: ", e);
        }
    }

    public void createHibernate(){
        Log.add(DEBUG_TAG,"Creating hibernate database", D);
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS rbl_data COLLATE utf8_general_ci");
            statement.execute("USE rbl_data");
            statement.execute("CREATE TABLE IF NOT EXISTS logic (logic_table_id INT(10) NOT NULL AUTO_INCREMENT,logic_id INT(10) NOT NULL,logic_name VARCHAR(50) NULL DEFAULT NULL,name VARCHAR(50) NULL DEFAULT NULL,PRIMARY KEY (logic_table_id))");
            statement.execute("CREATE TABLE IF NOT EXISTS actuator (actuator_table_id INT(10) NOT NULL AUTO_INCREMENT,actuator_id INT(10) NOT NULL,actuator_name VARCHAR(50) NULL DEFAULT NULL,actuator_type VARCHAR(50) NOT NULL,PRIMARY KEY (actuator_table_id))");
            statement.execute("CREATE TABLE IF NOT EXISTS logic_initiator (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_INIT_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_INIT_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
            statement.execute("CREATE TABLE IF NOT EXISTS logic_receiver (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not create hibernate database: ", e);
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Could not create hibernate database: ", e);
        }
    }

    public void selectDatabase(){
        if(D){Log.add(DEBUG_TAG,"selecting database", D);}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "USE " + Config.getConf().getString("database.db"));
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not select database: ", e);
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not select database: ", e);
        }
    }

    public void createTable(String name){
        if(D){Log.add(DEBUG_TAG,"Creating table", D);}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS " + name + "( temp INT )");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not create table: ", e);
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not create table: ", e);
        }
    }

    public void close(){
        if(D){Log.add(DEBUG_TAG,"Closing connection", D);}
        try {
            mConnection.close();
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not close connection: ", e);
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not close connection: ", e);
        }
    }

    public void insertTemp(int temp){
        if(D){Log.add(DEBUG_TAG,"Inserting temperature data", D);}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "INSERT INTO " + "test" + " (temp) VALUES (" + temp + ")");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "write test data: ", e);
        }catch (Exception e) {
            Log.add(DEBUG_TAG, "write test data: ", e);
        }
    }

    public void test() {
        if(D){Log.add(DEBUG_TAG,"Testing connection!", D);}
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute(
                    "INSERT INTO " + "test" + " (temp) VALUES (1.4),(2.5),(3.6)");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "write test data: ", e);
        }catch (Exception e) {
            Log.add(DEBUG_TAG, "write test data: ", e);
        }
    }
}
