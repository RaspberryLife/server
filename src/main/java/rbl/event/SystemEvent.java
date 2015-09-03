package rbl.event;

/**
 * Created by Peter Mösenthin.
 */
public class SystemEvent
{

	public enum Type
	{
		START_SYSTEM, STOP_SYSTEM, RESTART_SYSTEM,
		START_SERIAL_CONNECTION, STOP_SERIAL_CONNECTION, RESTART_SERIAL_CONNECTION,
		START_SCHEDULER, STOP_SCHEDULER, RESTART_SCHEDULER
	}

	private String message;
	private Type type;

	public SystemEvent(Type type)
	{
		this.type = type;
	}

	public SystemEvent(String message, Type type)
	{
		this.message = message;
		this.type = type;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}
}
