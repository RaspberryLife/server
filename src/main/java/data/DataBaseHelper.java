package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseHelper {

    public static MySQLConnection mySQLConnection;

    public static final String DEBUG_TAG = DataBaseHelper.class.getSimpleName();

    public List<? super Object> getDataList(int length){
        Log.add(DEBUG_TAG,"Generating some fake data");
        List<? super Object> dataList = new ArrayList<Object>();
        for (int i=0; i< length; i++){
            Random r = new Random();
            dataList.add((r.nextFloat()* 30) + i);
        }
        return dataList;
    }

    public static void init(){
        if(mySQLConnection == null){
            mySQLConnection = new MySQLConnection();
        }
        mySQLConnection.open();
        mySQLConnection.createDatabase();
        mySQLConnection.selectDatabase();
        mySQLConnection.createTable("temp");
    }

    public static void closeConnection(){
        mySQLConnection.close();
    }

    public static synchronized void writeTempData(int temp){
        init();
        mySQLConnection.insertTemp(temp);
        mySQLConnection.close();
    }

}
