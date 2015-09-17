package rbl.serial;

import com.google.common.eventbus.Subscribe;
import rbl.data.model.Module;
import rbl.event.NotificationEvent;
import rbl.event.SerialMessageEvent;
import rbl.system.service.DataBaseService;
import rbl.system.service.EventBusService;
import rbl.util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageHandler
{

	public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

	// Instruction ID - used to detect system types early on
	public static final int IID_DEBUG_BROADCAST = 99;
	public static final int IID_HEARTBEAT = 98;
	public static final int IID_MANAGE_ADDRESS = 95;

	//----------------------------------------------------------------------------------------------
	//                                      POPULATION & SYSTEM CHECK
	//----------------------------------------------------------------------------------------------

	@Subscribe
	public void handleSerialMessage(SerialMessageEvent message)
	{
		Log.add(DEBUG_TAG, "Received message " + message.getRawContent());
		message.buildModel();
		if (message.getMessageType() == SerialMessageEvent.Type.SEND)
		{
			switchOnInstruction(message);
		}
	}

	/**
	 * Pass message to IID method
	 *
	 * @param message
	 */
	private void switchOnInstruction(SerialMessageEvent message)
	{
		switch (message.getInstructionId())
		{
			case IID_DEBUG_BROADCAST:
				broadcastDebug(message);
				break;
			case IID_HEARTBEAT:
				readHeartbeat(message);
				break;
			case IID_MANAGE_ADDRESS:
				manageAddress(message);
				break;
		}
	}

	//----------------------------------------------------------------------------------------------
	//                                      SYSTEM FUNCTIONALITY
	//----------------------------------------------------------------------------------------------

	/**
	 * Handle module heartbeat message
	 *
	 * @param message
	 */
	private void readHeartbeat(SerialMessageEvent message)
	{
		String voltage = "";
		String preColon = message.getParameters().get(2).substring(0, 1);
		String postColon = message.getParameters().get(2).substring(2, 3);
		voltage = preColon + "," + postColon + "V";

		Log.add(DEBUG_TAG, "Received heartbeat "
						+ "Type: " + message.getModuleType()
						+ "Id: " + message.getModuleId()
						+ "State: " + message.getParameters().get(0)
						+ "Frequency: " + message.getParameters().get(1)
						+ "Battery voltage : " + voltage
		);
	}

	/**
	 * Handle address request by modules
	 *
	 * @param message
	 */
	private void manageAddress(SerialMessageEvent message)
	{
		if (message.getParameters().isEmpty())
		{ // Module is requesting
			Log.add(DEBUG_TAG, "Module requested address");
			if (message.getModuleType() == SerialTypeResolver.MODULE_OUTLET)
			{
				Log.add(DEBUG_TAG, "Requesting module is of type OUTLET. Not generating address");
			}
			else
			{
				String address = SerialAddress.generate();
				sendAddressMessage(message, address);
				Module m = new Module();
				m.setSerialAddress(address);
				m.setType(SerialTypeResolver.matchDatabaseType(message.getModuleType()));
				DataBaseService.getInstance().write(m);
			}
		}
		else
		{ // Module has address
			String address = message.getParameters().get(0);
			Log.add(DEBUG_TAG, "Module sent address: " + address);
			if (DataBaseService.getInstance().moduleExists(address))
			{
				sendAddressMessage(message, address);
				//TODO update database entry
			}
		}
	}

	/**
	 * Send a message containing the generated address to the requesting module
	 *
	 * @param message
	 * @param address
	 */
	private void sendAddressMessage(SerialMessageEvent message, String address)
	{
		SerialMessageEvent me = new SerialMessageEvent();
		me.setModuleType(message.getModuleType());
		me.setMessageType(SerialMessageEvent.Type.SEND);
		me.setModuleId(message.getModuleId());
		me.setInstructionId(IID_MANAGE_ADDRESS);
		//List modules = DataBaseService.getInstance().readAll(DataBaseService.DataType.MODULE);
		me.getParameters().add(address);
		EventBusService.post(me);
	}

	/**
	 * Notify every client with a message from a module
	 *
	 * @param message
	 */
	private void broadcastDebug(SerialMessageEvent message)
	{
		Log.add(DEBUG_TAG, "Received debug " + message.getRawContent());

		// Client broadcast
		String bc_message = "Serial connector received message: " + message.getRawContent();
		NotificationEvent e = new NotificationEvent(
				NotificationEvent.Type.CLIENT_BROADCAST, bc_message);
		EventBusService.post(e);

		// Module broadcast
		SerialMessageEvent me = new SerialMessageEvent();
		me.setMessageType(SerialMessageEvent.Type.SEND);
		me.setModuleType(message.getModuleType());
		me.setModuleId(message.getModuleId());
		me.setInstructionId(message.getInstructionId());
		me.setParameters(message.getParameters());
		EventBusService.post(me);
	}

}
