package rbl.data.model.logic;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rblAction")
public class Action
{
	public static String TYPE_NOTIFY = "notify";

	@Id
	@GeneratedValue
	@Column(name = "actionId")
	private int actionId;

	@Column(name = "actionUserId")
	private int actionUserId;

	@Column(name = "actionType")
	private String actionType;

	@Column(name = "actionMessage")
	private String actionMessage;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="logicId",
			insertable=false, updatable=false,
			nullable=false)
	private Logic actionLogic;

	public Logic getActionLogic()
	{
		return actionLogic;
	}

	public void setActionLogic(Logic logics)
	{
		this.actionLogic = logics;
	}

	public Action()
	{
	}

	public int getActionId()
	{
		return actionId;
	}

	public void setActionId(int id)
	{
		this.actionId = id;
	}

	public int getActionUserId()
	{
		return actionUserId;
	}

	public void setActionUserId(int user_id)
	{
		this.actionUserId = user_id;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String type)
	{
		this.actionType = type;
	}

	public String getActionMessage()
	{
		return actionMessage;
	}

	public void setActionMessage(String message)
	{
		this.actionMessage = message;
	}

	@Override public String toString()
	{
		return "Action{" +
				"actionId=" + actionId +
				", actionUserId=" + actionUserId +
				", actionType='" + actionType + '\'' +
				", actionMessage='" + actionMessage + '\'' +
				", logic=" + actionLogic +
				'}';
	}
}
