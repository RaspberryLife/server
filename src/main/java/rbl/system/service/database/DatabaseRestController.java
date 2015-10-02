package rbl.system.service.database;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rbl.data.model.Module;
import rbl.data.model.User;
import rbl.data.model.logic.Action;
import rbl.data.model.logic.Logic;
import rbl.data.model.logic.Trigger;
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
			@RequestParam(value = "logic") String jsonLogic)
	{
		Gson gson = new Gson();
		Logic gsonLogic = gson.fromJson(jsonLogic, Logic.class);

		Logic logic = new Logic();
		logic.setLogicName(gsonLogic.getLogicName());
		logic.setExecType(gsonLogic.getExecType());
		logic.setExecHour(gsonLogic.getExecHour());
		logic.setExecMinute(gsonLogic.getExecMinute());

		for(Trigger t : gsonLogic.getLogicTriggers()){
			Trigger trigger = new Trigger ();
			trigger.setTriggerLogic(logic);

			trigger.setTriggerFieldId(t.getTriggerFieldId());
			trigger.setTriggerState(t.isTriggerState());

			logic.getLogicTriggers().add(trigger);
		}

		for(Action a : gsonLogic.getLogicActions()){
			Action action = new Action();
			action.setActionLogic(logic);

			action.setActionMessage(a.getActionMessage());
			action.setActionUserId(a.getActionUserId());
			action.setActionType(a.getActionType());

			logic.getLogicActions().add(action);
		}
		db.write(logic);
	}


	/**
	 * Retrieve a list of all logics
	 *
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/logics", method = RequestMethod.GET)
	public List<Logic>  getLogicList()
	{
		List<Logic> logics = (List<Logic>)db.getList(DataBaseService.DataType.LOGIC);

		logics.forEach(logic -> logic.getLogicTriggers().forEach(trigger -> trigger.setTriggerLogic(null)));
		logics.forEach(logic -> logic.getLogicActions().forEach(trigger -> trigger.setActionLogic(null)));

		Log.add(DEBUG_TAG,"get logics: " +  logics.toString() + " ");
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
		u.setUserName(name);
		u.setUserEmail(email);
		u.setUserRole(role);
		u.setUserPassword(password);

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
			module.setModuleName(name);
			module.setModuleRoom(room);
			db.write(module);
		}
	}

	@RequestMapping(value = "/rbl/system/database/modules", method = RequestMethod.GET)
	public List getModules()
	{
		return db.getList(DataBaseService.DataType.MODULE);
	}




}
