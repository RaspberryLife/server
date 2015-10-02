package rbl.data.model.logic;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name = "rblLogic")
public class Logic
{

	public static final String EXECUTION_REQUIREMENT_SINGLE = "single";
	public static final String EXECUTION_REQUIREMENT_MAJORITY = "majority";
	public static final String EXECUTION_REQUIREMENT_ALL = "all";

	public static final String EXECUTION_FREQUENCY_IMMEDIATELY = "immediately";
	public static final String EXECUTION_FREQUENCY_MINUTELY = "minutely";
	public static final String EXECUTION_FREQUENCY_HOURLY = "hourly";
	public static final String EXECUTION_FREQUENCY_DAILY = "daily";
	public static final String EXECUTION_FREQUENCY_WEEKLY = "weekly";
	public static final String EXECUTION_FREQUENCY_MONTHLY = "monthly";

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "logicId")
	private int logicId = 0;

	@Column(name = "logicName")
	private String logicName;

	@Column(name = "execRequirement")
	private String execRequirement;

	@Column(name = "execType")
	private String execType;

	@Column(name = "execMinute")
	private int execMinute;

	@Column(name = "execHour")
	private int execHour;

	@Column(name = "execDay")
	private int execDay;

	@Column(name = "execWeek")
	private int execWeek;

	@OneToMany(fetch = FetchType.EAGER, mappedBy= "actionLogic", cascade = CascadeType.ALL)
	private Set<Action> logicActions = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy= "triggerLogic", cascade = CascadeType.ALL)
	private Set<Trigger> logicTriggers = new HashSet<>();


	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public Logic()
	{
	}

	public int getLogicId()
	{
		return logicId;
	}

	public void setLogicId(int id)
	{
		this.logicId = id;
	}

	public String getLogicName()
	{
		return logicName;
	}

	public void setLogicName(String name)
	{
		this.logicName = name;
	}

	public String getExecType()
	{
		return execType;
	}

	public void setExecType(String exectype)
	{
		this.execType = exectype;
	}

	public int getExecMinute()
	{
		return execMinute;
	}

	public void setExecMinute(int execminute)
	{
		this.execMinute = execminute;
	}

	public int getExecHour()
	{
		return execHour;
	}

	public void setExecHour(int exechour)
	{
		this.execHour = exechour;
	}

	public int getExecDay()
	{
		return execDay;
	}

	public void setExecDay(int execday)
	{
		this.execDay = execday;
	}

	public int getExecWeek()
	{
		return execWeek;
	}

	public void setExecWeek(int execweek)
	{
		this.execWeek = execweek;
	}

	public Set<Action> getLogicActions()
	{
		return logicActions;
	}

	public void setLogicActions(Set<Action> logicActions)
	{
		this.logicActions = logicActions;
	}

	public Set<Trigger> getLogicTriggers()
	{
		return logicTriggers;
	}

	public void setLogicTriggers(Set<Trigger> logicTriggers)
	{
		this.logicTriggers = logicTriggers;
	}

	@Override public String toString()
	{
		return "Logic{" +
				"logicId=" + logicId +
				", logicName='" + logicName + '\'' +
				", execRequirement='" + execRequirement + '\'' +
				", execType='" + execType + '\'' +
				", execMinute=" + execMinute +
				", execHour=" + execHour +
				", execDay=" + execDay +
				", execWeek=" + execWeek +
				", logicActions=" + logicActions +
				", logicTriggers=" + logicTriggers +
				'}';
	}

	public String getExecRequirement()
	{
		return execRequirement;
	}

	public void setExecRequirement(String executionRequirement)
	{
		this.execRequirement = executionRequirement;
	}

}
