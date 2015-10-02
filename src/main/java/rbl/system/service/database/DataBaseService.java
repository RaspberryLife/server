package rbl.system.service.database;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rbl.data.MySQLConnection;
import rbl.data.model.Module;
import rbl.data.model.logic.Action;
import rbl.data.model.logic.Logic;
import rbl.data.model.logic.Trigger;
import rbl.serial.SerialAddress;
import rbl.util.Log;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 * <p>
 * The DataBaseService is basically a wrapper to access the Database.
 */
@RestController
public class DataBaseService
{
	public final String DEBUG_TAG = DataBaseService.class.getSimpleName();

	private static final DataBaseService instance = new DataBaseService();

	private MySQLConnection databaseConnection;
	private SessionFactory sessionFactory;

	public enum CreationMode
	{
		CREATE, // creates the schema, destroying previous data.
		CREATE_DROP, //drop the schema at the end of the session.
		UPDATE, //update the schema.
		VALIDATE //validate the schema, makes no changes to the database.
	}

	public enum DataType
	{
		LOGIC,
		USER,
		MODULE,
	}

	//----------------------------------------------------------------------------------------------
	//                                      LIFECYCLE
	//----------------------------------------------------------------------------------------------

	public static DataBaseService getInstance()
	{
		return instance;
	}

	private DataBaseService()
	{
	}

	/**
	 * Initialize the database if necessary. Set mode to update if it exists
	 */
	public void init()
	{
		Log.add(DEBUG_TAG, "Starting...");
		if (dataBaseAvailable())
		{
			if (checkIfDatabaseExistsAndCreateIfNot())
			{
				initSession(CreationMode.UPDATE);
			}
			else
			{
				initSession(CreationMode.CREATE);
				writeDemoObjects();
			}
		}
		else
		{
			Log.add(DEBUG_TAG, "No database connection available");
		}
	}

	private void writeDemoObjects()
	{
		Module m1 = new Module();
		m1.setSerialAddress(SerialAddress.generate());
		m1.setModuleType(Module.TYPE_REED);
		m1.setModuleName("fenster1");
		m1.setModuleRoom("fablab");

		Module m2 = new Module();
		m2.setSerialAddress(SerialAddress.generate());
		m2.setModuleType(Module.TYPE_REED);
		m2.setModuleName("fenster2");
		m2.setModuleRoom("fablab");

		write(m1);
		write(m2);

		Logic l = new Logic();


	}

	//----------------------------------------------------------------------------------------------
	//                                      INSERT
	//----------------------------------------------------------------------------------------------

	/**
	 * Insert an Object into the database. Object must be from the package 'data.model'
	 *
	 * @param object
	 */
	public void write(Object object)
	{
		Integer id = null;
		try
		{
			Session session = getSessionFactory().openSession();
			session.beginTransaction();

			session.save(object);

			session.getTransaction().commit();
			session.flush();
			session.close();
		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Could not write data");
			e.printStackTrace();
		}
	}

	//----------------------------------------------------------------------------------------------
	//                                      READ
	//----------------------------------------------------------------------------------------------

	public List getAdminList(){
		String query = "from User where userRole=admin";
		Session session = getSessionFactory().openSession();
		List adminList = session.createQuery(query).list();
		session.close();
		return adminList;
	}

	/**
	 * Read a list of objects from the database. Reads all entries
	 *
	 * @param type
	 * @return
	 */
	public List getList(DataType type)
	{
		String query = null;
		switch (type)
		{
			case LOGIC:
				query = "from Logic";
				break;
			case USER:
				query = "from User";
				break;
			case MODULE:
				query = "from Module";
				break;
		}
		Session session = getSessionFactory().openSession();
		List list = session.createQuery(query).list();
		session.close();
		return list;
	}

	/**
	 * Read a specific object from the database. Identified by the id.
	 *
	 * @param type
	 * @param id
	 * @return
	 */
	public Object getById(DataType type, int id)
	{
		String query = "from ";
		switch (type)
		{
			case LOGIC:
				query += "Logic";
				break;
			case USER:
				query += "User";
				break;
			case MODULE:
				query += "Module";
				break;
		}
		query += " where id=" + id;
		Session session = getSessionFactory().openSession();
		Object o  = session.createQuery(query).uniqueResult();
		session.close();
		return o;
	}

	public boolean moduleExists(String serialAddress)
	{
		List l = getList(DataBaseService.DataType.MODULE);
		for (Object o : l)
		{
			if (((Module) o).getSerialAddress().equalsIgnoreCase(serialAddress))
			{
				return true;
			}
		}
		return false;
	}


	//----------------------------------------------------------------------------------------------
	//                                      CHECK
	//----------------------------------------------------------------------------------------------

	/**
	 * Checks if a database exists and creates one if not.
	 */
	private boolean checkIfDatabaseExistsAndCreateIfNot()
	{
		if (databaseConnection == null)
		{
			databaseConnection = new MySQLConnection();
		}
		if (!databaseConnection.databaseExists())
		{
			Log.add(DEBUG_TAG, "Database does not exist. Creating new.");
			createDatabase();
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Checks if the databaseservice can connect to a database.
	 */
	public boolean dataBaseAvailable()
	{
		if (databaseConnection == null)
		{
			databaseConnection = new MySQLConnection();
		}
		return databaseConnection.open();
	}

	/**
	 * Create the database.
	 */
	private void createDatabase()
	{
		if (databaseConnection.open())
		{
			databaseConnection.createDatabase();
			databaseConnection.close();
		}
	}

	//----------------------------------------------------------------------------------------------
	//                                      Hibernate
	//----------------------------------------------------------------------------------------------

	/**
	 * Initialize a new serial session. Create database if needed.
	 */
	private SessionFactory initSession(CreationMode mode)
	{

		String creationMode = null;
		switch (mode)
		{
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
		return sessionFactory = buildSessionFactory(creationMode);
	}

	/**
	 * Build Hibernate session factory
	 *
	 * @return
	 */
	private SessionFactory buildSessionFactory(String creationMode)
	{
		try
		{
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			if (creationMode != null && !creationMode.isEmpty())
			{
				configuration.setProperty("hibernate.hbm2ddl.auto", creationMode);
			}

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
					configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);

		}
		catch (Exception e)
		{
			Log.add(DEBUG_TAG, "Failed to create SessionFactory. ", e);
			//e.printStackTrace();
			return null;
		}
	}

	public SessionFactory getSessionFactory()
	{
		if(sessionFactory != null)
		{
			return sessionFactory;
		}else
		{
			return initSession(CreationMode.UPDATE);
		}
	}
}
