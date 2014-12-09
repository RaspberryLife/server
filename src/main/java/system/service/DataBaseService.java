package system.service;

import data.HibernateHandler;
import data.MySqlConnection;
import data.model.Actuator;
import data.model.Logic;
import event.DataBaseEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter Mösenthin.
 *
 * The DataBaseHelper is basically a wrapper to access the MySQL Database.
 */
public class DataBaseService {

    private static final DataBaseService instance = new DataBaseService();

    public final String DEBUG_TAG = DataBaseService.class.getSimpleName();

    private HibernateHandler hibernateHandler = new HibernateHandler();

    public static void register() {
        EventBusService.register(instance);
    }

    private DataBaseService(){
    }

    public void handleDataBaseEvent(DataBaseEvent e){
        switch(e.getMessage()){
            case DataBaseEvent.START_SESSION:
                hibernateHandler.initSession();
                break;
            case DataBaseEvent.RUN_TEST:
                runHibernateWriteTest();
                runHibernateReadTest();
                break;
        }
    }

    private void runHibernateWriteTest() {
        hibernateHandler.writeTestLogic();
    }

    private void runHibernateReadTest(){
        hibernateHandler.readLogic();
    }

}
