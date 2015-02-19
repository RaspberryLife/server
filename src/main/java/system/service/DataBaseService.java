package system.service;

import com.sun.corba.se.spi.ior.ObjectKey;
import data.MySQLConnection;
import data.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import util.Log;

import java.util.List;


/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseService {
    public final String DEBUG_TAG = DataBaseService.class.getSimpleName();

    private static final DataBaseService instance = new DataBaseService();

    private MySQLConnection databaseConnection;
    private SessionFactory sessionFactory;

    public enum CreationMode {
        CREATE, // creates the schema, destroying previous data.
        CREATE_DROP, //drop the schema at the end of the session.
        UPDATE, //update the schema.
        VALIDATE //validate the schema, makes no changes to the database.
    }

    public enum DataType{
        LOGIC,
        USER,
        MESSAGE,
        MODULE,
    }

    //----------------------------------------------------------------------------------------------
    //                                      LIFECYCLE
    //----------------------------------------------------------------------------------------------

    public static DataBaseService getInstance(){
        return instance;
    }

    private DataBaseService(){
    }

    public void init(){
        Log.add(DEBUG_TAG, "Starting...");
        if(dataBaseAvailable()){
            if(dataBaseSetUp()){
                initSession(CreationMode.UPDATE);
            } else {
                initSession(CreationMode.CREATE);
            }
        } else {
            Log.add(DEBUG_TAG, "No database connection available");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      CHECK
    //----------------------------------------------------------------------------------------------

    /**
     * Checks if a database exists and creates one if not.
     */
    private boolean dataBaseSetUp(){
        if(databaseConnection == null){
            databaseConnection = new MySQLConnection();
        }
        if(!databaseConnection.databaseExists()){
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
        if(databaseConnection == null){
            databaseConnection = new MySQLConnection();
        }
        return databaseConnection.open();
    }

    /**
     * Create the database.
     */
    private void createDatabase(){
        if(databaseConnection.open()){
            databaseConnection.createDatabase();
            databaseConnection.close();
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      Hibernate
    //----------------------------------------------------------------------------------------------


    /**
     * Initialize a new server session. Create database if needed.
     */
    private void initSession(CreationMode mode){

        String creationMode = null;
        switch (mode){
            case CREATE:
                creationMode = "create";
                break;
            case CREATE_DROP:
                creationMode = "create-drop";
                break;
            case UPDATE:
                creationMode = "update";
                break;
            case VALIDATE:
                creationMode = "validate";
                break;
        }
        Log.add(DEBUG_TAG, "Initializing session (MODE: " + creationMode + ")");
        sessionFactory = buildSessionFactory(creationMode);
    }



    /**
     * Build Hibernate session factory
     * @return
     */
    private SessionFactory buildSessionFactory(String creationMode) {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            if(creationMode != null && !creationMode.isEmpty()){
                configuration.setProperty("hibernate.hbm2ddl.auto", creationMode);
            }

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Failed to create SessionFactory. ", e);
            //e.printStackTrace();
            return null;
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //----------------------------------------------------------------------------------------------
    //                                      INSERT
    //----------------------------------------------------------------------------------------------

    public void insert(Object object){
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(object);

            session.getTransaction().commit();
            session.close();
        }catch (Exception e){
            Log.add(DEBUG_TAG, "Could not write data");
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      READ
    //----------------------------------------------------------------------------------------------


    public List readAll(DataType type){
        String query = null;
        switch (type){
            case LOGIC:
                query = "from Logic";
                break;
            case USER:
                query = "from User";
                break;
            case MESSAGE:
                query = "from NotificationMessage";
                break;
            case MODULE:
                query = "from Module";
                break;
        }
        Session session = sessionFactory.openSession();
        List list = session.createQuery(query).list();
        session.close();
        return list;
    }

    public List readId(DataType type, int id){
        String query = "from ";
        switch (type){
            case LOGIC:
                query += "Logic";
                break;
            case USER:
                query += "User";
                break;
            case MESSAGE:
                query += "NotificationMessage";
                break;
            case MODULE:
                query += "Module";
                break;
        }
        query += " where id=" + id;
        Session session = sessionFactory.openSession();
        List list = session.createQuery(query).list();
        session.close();
        return list;
    }

    public boolean moduleExists(String serialAddress){
        List l = readAll(DataBaseService.DataType.MODULE);
        for (Object o : l){
            if(((Module) o).getSerial_address().equalsIgnoreCase(serialAddress)){
                return true;
            }
        }
        return false;
    }

    public String generateSerialAddress(){
        List l = readAll(DataBaseService.DataType.MODULE);
        String base = "C2C2C2C2";
        String[] lead = {"A","B","C","D"};
        for(String s : lead){
            for(int i = 1; i <= 6; i++){
                String currentTry = base + s + i;
                boolean available = false;
                for (Object o : l){
                    available = ((Module) o).getSerial_address().equalsIgnoreCase(currentTry);
                }
                if(available){
                    return currentTry;
                }
            }
        }
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

        insert(l1);
    }

    public void readLogic(){
        for(Object o : readAll(DataType.LOGIC)){
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
