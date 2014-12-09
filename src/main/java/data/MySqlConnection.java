package data;

import util.Config;
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

    public boolean open(){
        Log.add(DEBUG_TAG,"Opening database connection");
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

        } catch(Exception e) {
            Log.add(DEBUG_TAG,"Could not open database: ", e);
            return false;
        }
        return true;
    }

    public boolean databaseExists(){
        ResultSet resultSet = null;
        String dbname = Config.getConf().getString("database.db");
        try {
            resultSet = mConnection.getMetaData().getCatalogs();

            while (resultSet.next()) {
                // Get the database name, which is at position 1
                String databaseName = resultSet.getString(1);
                if(databaseName.equals(dbname)){
                    return true;
                }
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void createInitialStructure(){
        createDatabase();
        selectDatabase();
        createLogicTable();
        createActuatorTable();
        createLogicInitiatorTable();
        createLogicReceiverTable();
    }

    public void createDatabase(){
        Log.add(DEBUG_TAG,"Creating database");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS rbl_data COLLATE utf8_general_ci");
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Could not create database: ", e);
        }
    }



    public void createHibernate(){
        Log.add(DEBUG_TAG,"Creating hibernate database");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();



            statement.execute("CREATE TABLE IF NOT EXISTS logic_initiator (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_INIT_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_INIT_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
            statement.execute("CREATE TABLE IF NOT EXISTS logic_receiver (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Could not create hibernate database: ", e);
        }
    }

    public void selectDatabase(){
        Log.add(DEBUG_TAG,"Selecting database");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("USE rbl_data");
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not select database: ", e);
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not select database: ", e);
        }
    }

    public void createLogicTable(){
        Log.add(DEBUG_TAG,"Creating logic table");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS logic (logic_table_id INT(10) NOT NULL AUTO_INCREMENT,logic_id INT(10) NOT NULL,logic_name VARCHAR(50) NULL DEFAULT NULL,PRIMARY KEY (logic_table_id))");
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not create logic table: ", e);
        }
    }

    public void createActuatorTable(){
        Log.add(DEBUG_TAG,"Creating actuator table");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS actuator (actuator_table_id INT(10) NOT NULL AUTO_INCREMENT,actuator_id INT(10) NOT NULL,actuator_name VARCHAR(50) NULL DEFAULT NULL,actuator_type VARCHAR(50) NOT NULL,PRIMARY KEY (actuator_table_id))");
        }catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not create table: ", e);
        }
    }

    public void createLogicInitiatorTable(){
        Log.add(DEBUG_TAG,"Creating logic_initiator table");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS logic_initiator (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_INIT_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_INIT_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
        }catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not create logic_initiator table: ", e);
        }
    }

    public void createLogicReceiverTable(){
        Log.add(DEBUG_TAG,"Creating logic_receiver table");
        Statement statement = null;
        try {
            statement = mConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS logic_receiver (actuator_table_id INT(10) NOT NULL,logic_table_id INT(10) NOT NULL,PRIMARY KEY (actuator_table_id, logic_table_id),CONSTRAINT FK_RECEIVE_ACTUATOR FOREIGN KEY (actuator_table_id) REFERENCES actuator (actuator_table_id),CONSTRAINT FK_RECEIVE_LOGIC FOREIGN KEY (logic_table_id) REFERENCES logic (logic_table_id))");
        }catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not create logic_receiver table: ", e);
        }
    }



    public void close(){
        Log.add(DEBUG_TAG,"Closing connection");
        try {
            mConnection.close();
        } catch (SQLException e) {
            Log.add(DEBUG_TAG, "Could not close connection: ", e);
        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Could not close connection: ", e);
        }
    }


}
