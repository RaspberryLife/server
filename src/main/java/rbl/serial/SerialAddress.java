package rbl.serial;

import rbl.data.model.Module;
import rbl.data.DataBaseService;
import rbl.util.Log;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialAddress
{
	public static final String DEBUG_TAG = SerialAddress.class.getSimpleName();

	/**
	 * Generates an address to identify a module during the serial communication
	 * Generates addresses in the form C2C2C2C2{A1-D6}
	 * @return
	 */
	public static String generate()
	{
		List knownModules = DataBaseService.getInstance().getList(DataBaseService.DataType.MODULE);

		// No module present yet
		if(knownModules == null || knownModules.size() == 0){
			return "C2C2C2C2A1";
		}

		String base = "C2C2C2C2";
		String[] lead = { "A", "B", "C", "D" };
		for (String currentLead : lead)
		{
			for (int currentfollow = 1; currentfollow <= 6; currentfollow++)
			{
				String currentTry = base + currentLead + currentfollow;
				boolean available = false;
				for (Object module : knownModules)
				{
					available = ((Module) module).getSerialAddress().equalsIgnoreCase(currentTry);
				}
				if (available)
				{
					Log.add(DEBUG_TAG, "Generated new serial address: " + currentTry);
					return currentTry;
				}
			}
		}
		return null;
	}
}
