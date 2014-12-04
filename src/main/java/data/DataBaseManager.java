package data;

import data.model.Actuator;
import data.model.Logic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseManager {

    private MySqlConnection mySqlConnection;
    private SessionFactory sessionFactory;

    public final String DEBUG_TAG = DataBaseManager.class.getSimpleName();


    private SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Exception e) {
            Log.add(DEBUG_TAG, "Failed to create SessionFactory. ", e);
            return null;
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void initSession(){
        if(mySqlConnection == null){
            mySqlConnection = new MySqlConnection();
        }
        if(mySqlConnection.open()){
            if(!mySqlConnection.databaseExists()){
                Log.add(DEBUG_TAG, "Database does not exist creating new.   ");
                createInitialStructure();
                mySqlConnection.close();
            }
        }
        sessionFactory = buildSessionFactory();
    }


    public void runHibernateWriteTest() {
        SessionFactory sf = getSessionFactory();
        Session session = sf.openSession();
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

    public void runHibernateReadTest(){
        Session session = sessionFactory.openSession();
        List logics = session.createQuery("from Logic").list();
        for(Object o : logics){
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
            Log.add(DEBUG_TAG, "Found logic: " + l.getName() + " Id: " + l.getLogic_id() + " Initiator: " + init + " Receiver: " + receiver);
        }
        session.close();
    }

    public List<? super Object> getFakeDataList(int length){
        Log.add(DEBUG_TAG, "Generating some fake data");
        List<? super Object> dataList = new ArrayList<Object>();
        for (int i=0; i< length; i++){
            Random r = new Random();
            dataList.add((r.nextFloat() * 30) + i);
        }
        return dataList;
    }

    public void createInitialStructure(){
        Log.add(DEBUG_TAG, "Creating Hibernate structure");
        if(mySqlConnection.open()){
            mySqlConnection.createInitialStructure();
            mySqlConnection.close();
        }
    }

}
