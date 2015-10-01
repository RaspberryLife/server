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

	@Column(name = "user_id")
	private int user_id;

	@Column(name = "type")
	private String type;

	@Column(name = "message")
	private String message;

	public Action()
	{
	}

	public int getAction_id()
	{
		return action_id;
	}

	public void setAction_id(int action_id)
	{
		this.action_id = action_id;
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
}
