package data;

import data.model.Actuator;
import data.model.Logic;
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

    public List readLogic(){
        Session session = sessionFactory.openSession();
        List logic_list = session.createQuery("from Logic").list();
        for(Object o : logic_list){
            Logic l = (Logic) o;
            String init = "[ ";
            String receiver = "[ ";
            for(Actuator a : l.getLogic_initiator()){
                init += a.getType() + " ";
                init += a.getActuator_id() + " ";
            }
            init += " ]";
            for(Actuator a : l.getLogic_receiver()){
                receiver += a.getType() + " ";
                receiver += a.getActuator_id() + " ";
            }
            receiver += " ]";
            Log.add(DEBUG_TAG,
                    "Found logic: " + l.getName()
                            + " Id: " + l.getLogic_id()
                            + " Initiator: " + init
                            + " Receiver: " + receiver);
        }
        session.close();
        return logic_list;
    }

    public void writeTestLogic(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Actuator a = new Actuator();
        a.setType(Actuator.TYPE_CLIENT);
        a.setActuator_id(66);
        Actuator b = new Actuator();
        b.setType(Actuator.TYPE_MODULE);
        b.setActuator_id(67);
        Actuator c = new Actuator();
        c.setType(Actuator.TYPE_SYSTEM);
        c.setActuator_id(1234);

        Logic l1 = new Logic();
        l1.setName("heizung_fenster");
        l1.setLogic_id(1234);
        l1.getLogic_initiator().add(a);
        l1.getLogic_receiver().add(a);

        Logic l2 = new Logic();
        l2.setLogic_id(5678);
        l2.setName("fenster_heizung");
        l2.getLogic_initiator().add(b);
        l2.getLogic_receiver().add(a);
        l2.getLogic_receiver().add(c);

        session.save(l1);
        session.save(l2);

        session.getTransaction().commit();
        session.close();
    }

}
