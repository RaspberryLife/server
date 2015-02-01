package data;

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
 */
public class HibernateHandler {

    public final String DEBUG_TAG = HibernateHandler.class.getSimpleName();

    private SessionFactory sessionFactory;
    private MySqlConnection mySqlConnection;


    /**
     * Initialize a new server session. Create database if needed.
     */
    public void initSession(){
        Log.add(DEBUG_TAG, "Initializing session");
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        String creationMode = null;

        if(mySqlConnection.open()){
            if(!mySqlConnection.databaseExists()){
                Log.add(DEBUG_TAG, "Database does not exist creating new.   ");
                createInitialStructure();
            }else {
                creationMode = "update";
            }
        }
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

    /**
     * Create basic database structure.
     */
    private void createInitialStructure(){
        if(mySqlConnection.open()){
            mySqlConnection.createDatabase();
            mySqlConnection.close();
        }
    }




    //----------------------------------------------------------------------------------------------
    //                                      TESTING
    //----------------------------------------------------------------------------------------------

    public void readLogic(){
        Log.add(DEBUG_TAG, "Running read test");
        Session session = sessionFactory.openSession();
        List logic_list = session.createQuery("from Logic").list();

        for(Object o : logic_list){
            Logic l = (Logic) o;
            //printLogic(l);
        }
        session.close();
    }

    public void writeTestLogic(){
        try {
            Log.add(DEBUG_TAG, "Running write test");
            Session session = sessionFactory.openSession();
            session.beginTransaction();

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

            //printLogic(l1);

            session.save(l1);

            session.getTransaction().commit();
            session.close();
        }catch (Exception e){
            e.printStackTrace();
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
