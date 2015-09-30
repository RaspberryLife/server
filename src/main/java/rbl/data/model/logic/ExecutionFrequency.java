package rbl.data.model.logic;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_execution_frequency")
public class ExecutionFrequency
{

	public static final String TYPE_IMMEDIATELY = "immediately";
	public static final String TYPE_MINUTELY = "minutely";
	public static final String TYPE_HOURLY = "hourly";
	public static final String TYPE_DAILY = "daily";
	public static final String TYPE_WEEKLY = "weekly";
	public static final String TYPE_MONTHLY = "monthly";

	@Id
	@GeneratedValue
	@Column(name = "execution_frequency_id")
	private int executionFrequencyId;

	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Logic logic;

	@Column(name = "type")
	private String type;

	@Column(name = "minute")
	private int minute;

	@Column(name = "hour")
	private int hour;

	@Column(name = "day")
	private int day;

	@Column(name = "week")
	private int week;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getExecutionFrequencyId()
	{
		return executionFrequencyId;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getMinute()
	{
		return minute;
	}

	public void setMinute(int minute)
	{
		this.minute = minute;
	}

	public int getHour()
	{
		return hour;
	}

	public void setHour(int hour)
	{
		this.hour = hour;
	}

	public int getDay()
	{
		return day;
	}

	public void setDay(int day)
	{
		this.day = day;
	}

	public int getWeek()
	{
		return week;
	}

	public void setWeek(int week)
	{
		this.week = week;
	}

	@Override public String toString()
	{
		return "ExecutionFrequency{" +
				"executionFrequencyId=" + executionFrequencyId +
				", type='" + type + '\'' +
				", minute=" + minute +
				", hour=" + hour +
				", day=" + day +
				", week=" + week +
				'}';
	}
}
