package system.service;

import data.HibernateHandler;
import data.MySqlConnection;
import data.model.*;
import util.Log;

import java.util.List;
import java.util.Objects;


/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseService {
    public final String DEBUG_TAG = DataBaseService.class.getSimpleName();

    private static final DataBaseService instance = new DataBaseService();

    private HibernateHandler hibernateHandler = new HibernateHandler();
    private MySqlConnection mySqlConnection;

    public static DataBaseService getInstance(){
        return instance;
    }

    private DataBaseService(){
    }

    public void start(){
        Log.add(DEBUG_TAG, "Starting...");
        if(dataBaseAvailable()){
            if(dataBaseSetUp()){
                hibernateHandler.initSession(HibernateHandler.CreationMode.UPDATE);
            } else {
                hibernateHandler.initSession(HibernateHandler.CreationMode.CREATE);
            }
        } else {
            Log.add(DEBUG_TAG, "No database connection available");
        }
    }

    /**
     * Checks if a database exists and creates one if not.
     */
    private boolean dataBaseSetUp(){
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        if(!mySqlConnection.databaseExists()){
            Log.add(DEBUG_TAG, "Database does not exist. Creating new.");
            createDatabase();
            return false;
        }else {
            return true;
        }
    }

    /**
     * Checks if the server can connect to a database.
     */
    private boolean dataBaseAvailable(){
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        return mySqlConnection.open();
    }

    /**
     * Create the database.
     */
    private void createDatabase(){
        if(mySqlConnection.open()){
            mySqlConnection.createDatabase();
            mySqlConnection.close();
        }
    }

    public void insert(Object o){
        hibernateHandler.insert(o);
    }

    //----------------------------------------------------------------------------------------------
    //                                      LOGIC
    //----------------------------------------------------------------------------------------------

    public List<Logic> readAllLogic(){
        return hibernateHandler.readList(HibernateHandler.DataType.LOGIC);
    }

    public Logic readLogic(int id){
        //TODO implement
        return null;
    }

    //----------------------------------------------------------------------------------------------
    //                                      USER
    //----------------------------------------------------------------------------------------------

    public List<User> readAllUser(){
        return hibernateHandler.readList(HibernateHandler.DataType.USER);
    }

    public User readUser(int id){
        //TODO implement
        return null;
    }

    //----------------------------------------------------------------------------------------------
    //                                      TEST
    //----------------------------------------------------------------------------------------------

    public void runTest(){
        writeTestLogic();
        readLogic();
    }


    public void writeTestLogic(){

        Actuator a = new Actuator();
        a.setType(Actuator.TYPE_CLIENT);
        a.setName("peters_nexus5");

        Actuator b = new Actuator();
        b.setType(Actuator.TYPE_MODULE);
        b.setName("Heizung-Wohnzimmer");

        Actuator c = new Actuator();
        c.setType(Actuator.TYPE_SYSTEM);
        c.setName("server_1.1.2");

        Logic l1 = new Logic();
        l1.setName("test_logic_666");
        l1.setExecution_requirement(Logic.EXECUTION_REQUIREMENT_ALL);

        ExecutionFrequency ef = new ExecutionFrequency();
        ef.setType(ExecutionFrequency.TYPE_DAILY);
        ef.setHour(17);
        ef.setMinute(40);

        l1.setExecution_frequency(ef);

        LogicInitiator li = new LogicInitiator();
        li.setActuator(a);
        Condition co = new Condition();
        co.setField_id(1);
        co.setThreshold_over(24);
        li.setCondition(co);

        LogicInitiator li1 = new LogicInitiator();
        li1.setActuator(b);
        Condition co1 = new Condition();
        co1.setField_id(2);
        co1.setThreshold_under(10);
        li1.setCondition(co1);

        LogicReceiver lr = new LogicReceiver();
        lr.setActuator(c);
        Instruction i = new Instruction();
        i.setField_id(12);
        i.setParameters("hallo du wurst");
        lr.setInstruction(i);

        l1.getLogic_initiator().add(li);
        l1.getLogic_initiator().add(li1);
        l1.getLogic_receiver().add(lr);

        hibernateHandler.insert(l1);
    }

    public void readLogic(){
        for(Object o : hibernateHandler.readList(HibernateHandler.DataType.LOGIC)){
            Logic l = (Logic) o;
            printLogic(l);
        }
    }

    public void printLogic(Logic l){
        try {
            String init = "[";
            String rec = "[";

            if (l.getLogic_initiator() != null && l.getLogic_initiator().size() > 0) {
                for (LogicInitiator lii : l.getLogic_initiator()) {
                    init += "(";
                    init += " Actuator: " + lii.getActuator().getName();
                    init += " Condition (fid): " + lii.getCondition().getField_id();
                    init += ")";
                }
            }

            init += "]";
            if (l.getLogic_receiver() != null && l.getLogic_receiver().size() > 0) {
                for (LogicReceiver lrr : l.getLogic_receiver()) {
                    rec += "(";
                    rec += " Actuator: " + lrr.getActuator().getName();
                    rec += " Instruction (fid)" + lrr.getInstruction().getField_id();
                    rec += ")";
                }
            }
            rec += "]";

            String freq = "[";
            freq += " Type: " + l.getExecution_frequency().getType();
            freq += " Hour: " + l.getExecution_frequency().getHour();
            freq += " Minute: " + l.getExecution_frequency().getMinute();
            freq += "]";

            Log.add(DEBUG_TAG,
                    "Found logic: " + l.getName()
                            + " Id: " + l.getLogic_id()
                            + " Frequency: " + freq
                            + " Initiator: " + init
                            + " Receiver: " + rec);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
