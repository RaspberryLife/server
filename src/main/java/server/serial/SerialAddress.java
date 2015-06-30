package server.serial;

import data.model.Module;
import system.service.DataBaseService;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialAddress
{

	/**
	 * Generates an address to identify a module during the serial communication
	 *
	 * @return
	 */
	public static String generate()
	{
		List l = DataBaseService.getInstance().readAll(DataBaseService.DataType.MODULE);
		String base = "C2C2C2C2";
		String[] lead = { "A", "B", "C", "D" };
		for (String s : lead)
		{
			for (int i = 1; i <= 6; i++)
			{
				String currentTry = base + s + i;
				boolean available = false;
				for (Object o : l)
				{
					available = ((Module) o).getSerial_address().equalsIgnoreCase(currentTry);
				}
				if (available)
				{
					return currentTry;
				}
			}
		}
		return null;
	}
}
