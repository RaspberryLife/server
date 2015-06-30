package event;

import org.quartz.Job;
import scheduling.RepeatInterval;

import java.util.HashMap;

/**
 * Created by Peter Mösenthin.
 */
public class ScheduleEvent
{

	public enum Type
	{
		START_TIME_LOG,
		START_RESOURCE_LOG,
		REBUILD_DATABASE,
		EXTENSION
	}

	private Type type;
	private String identity;
	private HashMap<RepeatInterval, Integer> interval = new HashMap<>();
	private Job job;

	public ScheduleEvent(Type type)
	{
		this.type = type;
	}

	public ScheduleEvent(String identity, Type type)
	{
		this.identity = identity;
		this.type = type;
	}

	public Job getJob()
	{
		return job;
	}

	public void setJob(Job job)
	{
		this.job = job;
	}

	public HashMap<RepeatInterval, Integer> getInterval()
	{
		return interval;
	}

	public void setInterval(HashMap<RepeatInterval, Integer> interval)
	{
		this.interval = interval;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getIdentity()
	{
		return identity;
	}

	public void setIdentity(String identity)
	{
		this.identity = identity;
	}

}
