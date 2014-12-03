package data;

import data.model.Actuator;
import data.model.Logic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class HibernateManager {

    private final SessionFactory sessionFactory = buildSessionFactory();

    private SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public void runTest() {
        SessionFactory sf = getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();

        Actuator a = new Actuator();
        a.setType(Actuator.TYPE_CLIENT);
        a.setActuator_id(42);
        Actuator b = new Actuator();
        b.setType(Actuator.TYPE_MODULE);
        b.setActuator_id(43);

        Logic l1 = new Logic();
        l1.setName("heizung_fenster");
        l1.setLogic_id(1234);
        l1.getLogic_initiator().add(a);
        l1.getLogic_receiver().add(b);

        Logic l2 = new Logic();
        l2.setLogic_id(5678);
        l2.setName("fenster_heizung");
        l2.getLogic_initiator().add(b);
        l2.getLogic_receiver().add(a);

        session.save(l1);
        session.save(l2);

        session.getTransaction().commit();
        session.close();

    }


}
