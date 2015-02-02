package data;

import data.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import protobuf.RblProto;
import util.Log;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
public class HibernateHandler {

    public final String DEBUG_TAG = HibernateHandler.class.getSimpleName();

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
        MESSAGE
    }

    /**
     * Initialize a new server session. Create database if needed.
     */
    public void initSession(CreationMode mode){

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

    public void insertList(List<Object> list){

    }

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


    public List readList(DataType type){
        String query = null;
        switch (type){
            case LOGIC:
                query = "from Logic";
                break;
            case USER:
                break;
            case MESSAGE:
                break;
        }
        Session session = sessionFactory.openSession();
        List list = session.createQuery(query).list();
        session.close();
        return list;
    }

    public Object read(DataType type){
        switch (type){
            case LOGIC:
                break;
            case USER:
                break;
            case MESSAGE:
                break;
        }

        return null;
    }

    //----------------------------------------------------------------------------------------------
    //                                      UPDATE
    //----------------------------------------------------------------------------------------------



    //----------------------------------------------------------------------------------------------
    //                                      DELETE
    //----------------------------------------------------------------------------------------------




}
