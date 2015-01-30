package data;

import data.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
public class HibernateHandler {

    private SessionFactory sessionFactory;
    private MySqlConnection mySqlConnection;

    public final String DEBUG_TAG = HibernateHandler.class.getSimpleName();


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
                mySqlConnection.close();
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
        Log.add(DEBUG_TAG, "Creating database");
        if(mySqlConnection.open()){
            mySqlConnection.createDatabase();
            mySqlConnection.close();
        }
    }




    //----------------------------------------------------------------------------------------------
    //                                      TESTING
    //----------------------------------------------------------------------------------------------

    public void readLogic(){
        Session session = sessionFactory.openSession();
        List logic_list = session.createQuery("from Logic").list();

        for(Object o : logic_list){
            Logic l = (Logic) o;
            String init  = "[";
            String rec = "[";

            for(LogicInitiator li : l.getLogic_initiator()){
                init += "(";
                init += " Actuator: " + li.getActuator().getName();
                init += " Condition (fid): " + li.getCondition().getField_id();
                init += ")";
            }
            init += "]";


            for(LogicReceiver lr : l.getLogic_receiver()){
                rec += "(";
                rec += " Actuator: " + lr.getActuator().getName();
                rec += " Instruction (fid)" + lr.getInstruction().getField_id();
                rec += ")";
            }
            rec += "]";

            String freq = "[";
            freq += " Type: " + l.getExecution_frequency().getExecution_frequency_type();
            freq += " Hour: " + l.getExecution_frequency().getExecution_frequency_hour();
            freq += " Minute: " + l.getExecution_frequency().getExecution_frequency_minute();
            freq += "]";

            Log.add(DEBUG_TAG,
                    "Found logic: " + l.getName()
                            + " Id: " + l.getLogic_id()
                            + " Frequency: " + freq
                            + " Initiator: " + init
                            + " Receiver: " + rec);
        }
        session.close();
    }

    public void writeTestLogic(){
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
        ef.setExecution_frequency_type(ExecutionFrequency.TYPE_DAILY);
        ef.setExecution_frequency_hour(17);
        ef.setExecution_frequency_minute(40);

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
        co.setField_id(2);
        co.setThreshold_under(10);
        li1.setCondition(co1);

        LogicReceiver lr = new LogicReceiver();
        lr.setActuator(c);
        Instruction i = new Instruction();
        i.setField_id(12);
        i.setParameters("hallo du wurst");

        Set<LogicInitiator> liset = new HashSet<LogicInitiator>();
        liset.add(li);
        liset.add(li1);
        Set<LogicReceiver> lirec = new HashSet<LogicReceiver>();
        lirec.add(lr);

        l1.setLogic_initiator(liset);
        l1.setLogic_receiver(lirec);

        session.save(l1);

        session.getTransaction().commit();
        session.close();
    }
}
