package rbl.data.model.logic;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_action")
public class Action
{
	public static String TYPE_NOTIFY = "notify";

	@Id
	@GeneratedValue
	@Column(name = "action_id")
	private int action_id;

	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Logic logic;

	@Column(name = "user_id")
	private int user_id;

	@Column(name = "type")
	private String type;

	@Column(name = "message")
	private String message;

	public Action()
	{
	}

	public Action(int user_id, String type, String message)
	{
		this.user_id = user_id;
		this.type = type;
		this.message = message;
	}

	public int getAction_id()
	{
		return action_id;
	}

	public void setAction_id(int action_id)
	{
		this.action_id = action_id;
	}

	public Logic getLogic()
	{
		return logic;
	}

	public void setLogic(Logic logic)
	{
		this.logic = logic;
	}

	public int getUser_id()
	{
		return user_id;
	}

	public void setUser_id(int user_id)
	{
		this.user_id = user_id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
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

	@Override public String toString()
	{
		return "Action{" +
				"action_id=" + action_id +
				", logic=" + logic +
				", user_id=" + user_id +
				", type='" + type + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
