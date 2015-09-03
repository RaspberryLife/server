package rbl.serial;

import rbl.data.model.Module;

/**
 * Created by Peter Mösenthin.
 */
public class SerialTypeResolver
{

	// Module type int
	public static final int MODULE_OUTLET = 1;
	public static final int MODULE_TEMP = 2;
	public static final int MODULE_PIR = 3;
	public static final int MODULE_REED = 4;
	public static final int MODULE_LUMINOSITY = 5;
	public static final int MODULE_RELAY = 6;
	public static final int MODULE_PIR_AND_RELAY = 7;
	public static final int MODULE_STATUS_MONITOR = 8;

	/**
	 * Matches the serial module type to the database model
	 *
	 * @param message
	 */
	public static String matchDatabaseType(int type)
	{
		switch (type)
		{
			case SerialTypeResolver.MODULE_OUTLET:
				return Module.TYPE_OUTLET;
			case SerialTypeResolver.MODULE_TEMP:
				return Module.TYPE_TEMP;
			case SerialTypeResolver.MODULE_PIR:
				return Module.TYPE_PIR;
			case SerialTypeResolver.MODULE_REED:
				return Module.TYPE_REED;
			case SerialTypeResolver.MODULE_LUMINOSITY:
				return Module.TYPE_LUMINOSITY;
			case SerialTypeResolver.MODULE_RELAY:
				return Module.TYPE_RELAY;
			case SerialTypeResolver.MODULE_PIR_AND_RELAY:
				return Module.TYPE_PIR_AND_RELAY;
		}
		return null;
	}

}
