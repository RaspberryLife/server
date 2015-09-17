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
import rbl.data.model.*;
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

	@RequestMapping(value = "/rbl/system/database/available", method = RequestMethod.GET)
	public boolean databaseAvailable(){
		return dataBaseAvailable();
	}

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

	@RequestMapping(value = "/rbl/system/database/user2")
	public void writeUser2(
			@RequestParam(value = "user", required = false) String userJSON)
	{
		Gson gson = new Gson();
		User user = gson.fromJson(userJSON, User.class);
		write(user);
	}

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

	//----------------------------------------------------------------------------------------------
	//                                      TEST
	//----------------------------------------------------------------------------------------------

	public void runTest()
	{
		if(dataBaseAvailable()){
			writeTestLogic();
			readLogic();
		}
	}

	public void writeTestLogic()
	{
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
		l1.setExecutionRequirement(Logic.EXECUTION_REQUIREMENT_ALL);

		ExecutionFrequency ef = new ExecutionFrequency();
		ef.setType(ExecutionFrequency.TYPE_DAILY);
		ef.setHour(17);
		ef.setMinute(40);

		l1.setExecutionFrequency(ef);

		LogicInitiator li = new LogicInitiator();
		li.setActuator(a);
		Condition co = new Condition();
		co.setFieldId(1);
		co.setThresholdOver(24);
		li.setCondition(co);

		LogicInitiator li1 = new LogicInitiator();
		li1.setActuator(b);
		Condition co1 = new Condition();
		co1.setFieldId(2);
		co1.setThresholdUnder(10);
		li1.setCondition(co1);

		LogicReceiver lr = new LogicReceiver();
		lr.setActuator(c);
		Instruction i = new Instruction();
		i.setFieldId(12);
		i.setParameters("hallo du wurst");
		lr.setInstruction(i);

		l1.getLogicInitiators().add(li);
		l1.getLogicInitiators().add(li1);
		l1.getLogicReceivers().add(lr);

		write(l1);
	}

	public void readLogic()
	{
		for (Object o : readAll(DataType.LOGIC))
		{
			Logic l = (Logic) o;
			printLogic(l);
		}
	}

	public void printLogic(Logic l)
	{
		try
		{
			String init = "[";
			String rec = "[";

			if (l.getLogicInitiators() != null && l.getLogicInitiators().size() > 0)
			{
				for (LogicInitiator lii : l.getLogicInitiators())
				{
					init += "(";
					init += " Actuator: " + lii.getActuator().getName();
					init += " Condition (fid): " + lii.getCondition().getFieldId();
					init += ")";
				}
			}

			init += "]";
			if (l.getLogicReceivers() != null && l.getLogicReceivers().size() > 0)
			{
				for (LogicReceiver lrr : l.getLogicReceivers())
				{
					rec += "(";
					rec += " Actuator: " + lrr.getActuator().getName();
					rec += " Instruction (fid)" + lrr.getInstruction().getFieldId();
					rec += ")";
				}
			}
			rec += "]";

			String freq = "[";
			freq += " Type: " + l.getExecutionFrequency().getType();
			freq += " Hour: " + l.getExecutionFrequency().getHour();
			freq += " Minute: " + l.getExecutionFrequency().getMinute();
			freq += "]";

			Log.add(DEBUG_TAG,
					"Found logic: " + l.getName()
							+ " Id: " + l.getLogic_id()
							+ " Frequency: " + freq
							+ " Initiator: " + init
							+ " Receiver: " + rec);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
