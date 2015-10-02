package rbl.system.service.database;

import com.google.gson.Gson;
import org.json.simple.parser.JSONParser;
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
		JSONParser parser = new JSONParser();

		Gson gson = new Gson();
		Logic gsonLogic = gson.fromJson(logic, Logic.class);

		Log.add(DEBUG_TAG, gsonLogic.getLogicTriggers().getClass().getSimpleName());

//		JsonElement logicElement = new JsonParser().parse(logic);
//		JsonObject logicobject = logicElement.getAsJsonObject();
//		JsonArray triggers = logicobject.getAsJsonArray("logicTriggers");
//
//		for(JsonElement j : triggers){
//			Log.add(DEBUG_TAG, "add trigger");
//			JsonObject t = j.getAsJsonObject();
//			Trigger trigger = new Trigger();
//			trigger.setTriggerModuleId(t.get("triggerModuleId").getAsInt());
//			trigger.setTriggerFieldId(t.get("triggerFieldId").getAsInt());
//			trigger.setTriggerState(t.get("triggerState").getAsBoolean());
//			Log.add(DEBUG_TAG, trigger.toString());
//			gsonLogic.getLogicTriggers().add(trigger);
//		}


//		try
//		{
//			Object o = parser.parse(logic);
//			JSONObject jsonLogic = (JSONObject)o;
//			Log.add(DEBUG_TAG, jsonLogic.toString());
//			Log.add(DEBUG_TAG,jsonLogic.get("logicTriggers").toString());
//
//			JSONArray triggers = (JSONArray) jsonLogic.get("logicTriggers");
//			JSONArray actions = (JSONArray) jsonLogic.get("logicActions");
//			gso
//			for( Object trigger : triggers){
//				Trigger t = gson.fromJson(trigger.toString(), Trigger.class);
//				gsonLogic.getLogicTriggers().add(t);
//			}
//		}
//		catch (ParseException e)
//		{
//			e.printStackTrace();
//		}


		Log.add(DEBUG_TAG, "write logic: " + gsonLogic.toString());
		db.write(gsonLogic);
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
