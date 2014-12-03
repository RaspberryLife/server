package data;

import util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseHandler {

    public MySqlConnection mySqlConnection;

    public final String DEBUG_TAG = DataBaseHandler.class.getSimpleName();

    public List<? super Object> getDataList(int length){
        Log.add(DEBUG_TAG, "Generating some fake data");
        List<? super Object> dataList = new ArrayList<Object>();
        for (int i=0; i< length; i++){
            Random r = new Random();
            dataList.add((r.nextFloat()* 30) + i);
        }
        return dataList;
    }

    public void setUpInitial(){
        init();
        closeConnection();
    }

    public void init(){
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        if(mySqlConnection.open()){
            mySqlConnection.createDatabase();
            mySqlConnection.selectDatabase();
            mySqlConnection.createTable("temp");
        }
    }

    public void closeConnection(){
        mySqlConnection.close();
    }

    public void createHibernateDataBase(){
        Log.add(DEBUG_TAG, "Preparing database for hibernate");
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        if(mySqlConnection.open()){
            mySqlConnection.createHibernate();
            mySqlConnection.close();
        }
    }

}
