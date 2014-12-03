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

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public void runTest() {
        SessionFactory sf = getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();

        Actuator a = new Actuator(0, Actuator.TYPE_MODULE);
        Actuator b = new Actuator(1, Actuator.TYPE_MODULE);

        Logic l1 = new Logic();
        l1.setName("heizung_fenster");
        l1.getInitiator().add(a);
        l1.getReceiver().add(b);

        Logic l2 = new Logic();
        l2.setName("fenster_heizung");
        l2.getInitiator().add(b);
        l2.getReceiver().add(a);

        session.save(l1);
        session.save(l2);

        session.getTransaction().commit();
        session.close();

    }


}
