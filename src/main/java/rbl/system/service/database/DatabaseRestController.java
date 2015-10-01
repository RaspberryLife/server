package rbl.system.service.database;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rbl.data.model.Module;
import rbl.data.model.User;
import rbl.data.model.logic.Logic;
import rbl.util.Log;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
@RestController
public class DatabaseRestController
{

	public final String DEBUG_TAG = DatabaseRestController.class.getSimpleName();

	DataBaseService db;

	public DatabaseRestController()
	{
		this.db = DataBaseService.getInstance();
	}

	/**
	 * Request a list of Admin users
	 *
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/adminusers", method = RequestMethod.GET)
	public List getAdminUsers()
	{
		return db.getAdminList();
	}

	@RequestMapping(value = "/rbl/system/database/logic", method = RequestMethod.POST)
	public void addLogic(
			@RequestParam(value = "logic") String logic)
	{
		Log.add(DEBUG_TAG, "Writing new logic");
		Log.add(DEBUG_TAG, logic);

		Gson gson = new Gson();

		Logic l = gson.fromJson(logic, Logic.class);
//		for(Trigger t : l.getTriggers()){
//			t.setModule((Module) db.getById(DataBaseService.DataType.MODULE, t.getModule().getId()));
//		}
		db.write(l);

		//Log.add(DEBUG_TAG, l.toString());
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
	 *
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/logics", method = RequestMethod.GET)
	public List getLogicList()
	{

		List logics = db.getList(DataBaseService.DataType.LOGIC);
		Log.add(DEBUG_TAG, logics.toString());
		return logics;
	}

	/**
	 * Add a user with the specified params
	 *
	 * @param name
	 * @param email
	 * @param role
	 * @param password
	 */
	@RequestMapping(value = "/rbl/system/database/user", method = RequestMethod.POST)
	public User writeUser(
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

		db.write(u);
		return u;
	}

	@RequestMapping(value = "/rbl/system/database/updatemodule", method = RequestMethod.POST)
	public void updateModule(
			@RequestParam(value = "id") int id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "room") String room)
	{
		Module module = (Module) db.getById(DataBaseService.DataType.MODULE, id);
		if (module != null)
		{
			module.setName(name);
			module.setRoom(room);
			db.write(module);
		}
	}

	@RequestMapping(value = "/rbl/system/database/modules", method = RequestMethod.GET)
	public List getModules()
	{
		return db.getList(DataBaseService.DataType.MODULE);
	}

}
