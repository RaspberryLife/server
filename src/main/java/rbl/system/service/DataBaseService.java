package rbl.system.service;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rbl.data.MySQLConnection;
import rbl.data.model.Module;
import rbl.data.model.User;
import rbl.data.model.logic.Logic;
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
		MESSAGE,
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
			}
		}
		else
		{
			Log.add(DEBUG_TAG, "No database connection available");
		}
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
	private boolean dataBaseAvailable()
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
	//                                      Rest
	//----------------------------------------------------------------------------------------------

	/**
	 * Check if a database is availiable to the server
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/available", method = RequestMethod.GET)
	public boolean databaseAvailable(){
		return dataBaseAvailable();
	}

	/**
	 * Request a list of Admin users
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/adminusers", method = RequestMethod.GET)
	public List getAdminUsers(){
		if(dataBaseAvailable()){
			String query = "from User where role=admin";
			Session session = sessionFactory.openSession();
			List adminList = session.createQuery(query).list();
			session.close();
			return adminList;
		}else {
			return null;
		}
	}

	//get list of all actuators
	public List<Module> getActuators () {
		return null;
	}

	@RequestMapping(value = "/rbl/system/database/logic", method = RequestMethod.POST)
	public void addLogic(
			@RequestParam(value = "logic") String logic){
		Log.add(DEBUG_TAG, "Writing new logic");
		Log.add(DEBUG_TAG, logic);

		Gson gson = new Gson();

		Logic l = gson.fromJson(logic, Logic.class);

		write(l);

		Log.add(DEBUG_TAG, l.toString());
//
//		Logic l = new Logic();
//
//		l.setName(name);
//
//		l.setExecutionRequirement(executionRequirement);
//
//		ExecutionFrequency e = new ExecutionFrequency();
//		e.setType(ExecutionFrequency.TYPE_DAILY);
//		e.setHour(17);
//		e.setMinute(50);
//		l.setExecutionFrequency(e);
//
//		Set<Action> actionSet = new HashSet<>();
//		actionSet.add(new Action(0,"notify","du hund!"));
//		l.setActions(actionSet);
//
//		Set<Trigger> triggerSet = new HashSet<>();
//
//		Module m = new Module();
//		m.setName("window1");
//		m.setSerialAddress("2840923842");
//		m.setType(Module.TYPE_PIR);
//
//		Condition c = new Condition();
//		c.setFieldId(3);
//		c.setThresholdOver(5);
//
//		triggerSet.add(new Trigger(m,c));
//		l.setTriggers(triggerSet);

	}

	/**
	 * Retrieve a list of all logics
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/logiclist", method = RequestMethod.GET)
	public List getLogicList(){
		if(dataBaseAvailable()){
			String query = "from Logic";
			Session session = sessionFactory.openSession();
			List list = session.createQuery(query).list();
			session.close();
			return list;
		}else {
			return null;
		}
	}

	/**
	 * Add a user with the specified params
	 * @param name
	 * @param email
	 * @param role
	 * @param password
	 */
	@RequestMapping(value = "/rbl/system/database/user", method = RequestMethod.POST)
	public void writeUser(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "password", required = false) String password)
	{
		User u = new User();
		u.setName(name);
		u.setEmail(email);
		u.setRole(role);
		u.setPassword(password);

		write(u);
	}


	//----------------------------------------------------------------------------------------------
	//                                      Hibernate
	//----------------------------------------------------------------------------------------------

	/**
	 * Initialize a new serial session. Create database if needed.
	 */
	private void initSession(CreationMode mode)
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
		sessionFactory = buildSessionFactory(creationMode);
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
		return sessionFactory;
	}

	//----------------------------------------------------------------------------------------------
	//                                      INSERT
	//----------------------------------------------------------------------------------------------

	/**
	 * Insert an Object into the database. Object must be from the package 'data.model'
	 *
	 * @param object
	 */
	public boolean write(Object object)
	{
		if(dataBaseAvailable())
		{
			initSession(CreationMode.UPDATE);
			try
			{
				Session session = sessionFactory.openSession();
				session.beginTransaction();

				session.save(object);

				session.getTransaction().commit();
				session.close();
				return true;
			}
			catch (Exception e)
			{
				Log.add(DEBUG_TAG, "Could not write data");
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	//----------------------------------------------------------------------------------------------
	//                                      READ
	//----------------------------------------------------------------------------------------------

	/**
	 * Read a list of objects from the database. Reads all entries
	 *
	 * @param type
	 * @return
	 */
	public List readAll(DataType type)
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

	/**
	 * Read a specific object from the database. Identified by the id.
	 *
	 * @param type
	 * @param id
	 * @return
	 */
	public List readById(DataType type, int id)
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

	public boolean moduleExists(String serialAddress)
	{
		List l = readAll(DataBaseService.DataType.MODULE);
		for (Object o : l)
		{
			if (((Module) o).getSerialAddress().equalsIgnoreCase(serialAddress))
			{
				return true;
			}
		}
		return false;
	}

}
