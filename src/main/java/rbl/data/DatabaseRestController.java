package rbl.data;

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
import rbl.event.EventBusService;
import rbl.event.ScheduleEvent;
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

		Log.add(DEBUG_TAG, "Received logic " + gsonLogic.toString());

		Logic logic = new Logic();
		logic.setLogicName(gsonLogic.getLogicName());
		logic.setExecFrequency(gsonLogic.getExecFrequency());
		logic.setExecRequirement(gsonLogic.getExecRequirement());
		logic.setExecHour(gsonLogic.getExecHour());
		logic.setExecMinute(gsonLogic.getExecMinute());

		for(Trigger t : gsonLogic.getLogicTriggers()){
			Trigger trigger = new Trigger ();
			trigger.setTriggerLogic(logic);

			trigger.setTriggerModuleId(t.getTriggerModuleId());
			trigger.setTriggerFieldId(t.getTriggerFieldId());
			trigger.setTriggerState(t.isTriggerState());
			trigger.setTriggerThresholdOver(t.getTriggerThresholdOver());
			trigger.setTriggerThresholdUnder(t.getTriggerThresholdUnder());

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

		Log.add(DEBUG_TAG, "Writing logic " + logic.toString());
		db.write(logic);
		EventBusService.post(new ScheduleEvent(ScheduleEvent.Type.REBUILD_DATABASE));
	}


	/**
	 * Retrieve a list of all logics
	 *
	 * @return
	 */
	@RequestMapping(value = "/rbl/system/database/logics", method = RequestMethod.GET)
	public List  getLogicList()
	{
		List<Object> logics = db.getList(DataBaseService.DataType.LOGIC);
		Log.add(DEBUG_TAG,"Get logics: Found: " + logics.size());
		logics.forEach(logic -> {
			((Logic) logic).getLogicTriggers().forEach(
					trigger -> trigger.setTriggerLogic(null)
			);
			((Logic) logic).getLogicActions().forEach(
					action -> action.setActionLogic(null)
			);
		});
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
