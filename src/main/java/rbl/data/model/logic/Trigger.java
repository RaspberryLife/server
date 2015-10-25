package rbl.data.model.logic;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rblTrigger")
public class Trigger
{
	@Id
	@GeneratedValue
	@Column(name = "triggerId")
	private int triggerId;

	@Column(name = "moduleId")
	private int triggerModuleId;

	@Column(name = "fieldId")
	private int triggerFieldId;

	@Column(name = "thresholdOver")
	private int triggerThresholdOver;

	@Column(name = "thresholdUnder")
	private int triggerThresholdUnder;

	@Column(name = "state")
	private boolean triggerState;

	@ManyToOne
	@JoinColumn(name="logicId")
	private Logic triggerLogic;

	public Trigger()
	{
	}

	public Trigger(int triggerModuleId, int triggerFieldId, int triggerThresholdOver, int triggerThresholdUnder,
			boolean triggerState)
	{
		this.triggerModuleId = triggerModuleId;
		this.triggerFieldId = triggerFieldId;
		this.triggerThresholdOver = triggerThresholdOver;
		this.triggerThresholdUnder = triggerThresholdUnder;
		this.triggerState = triggerState;
	}

	public int getTriggerId()
	{
		return triggerId;
	}

	public void setTriggerId(int id)
	{
		this.triggerId = id;
	}

	public int getTriggerModuleId()
	{
		return triggerModuleId;
	}

	public void setTriggerModuleId(int moduleId)
	{
		this.triggerModuleId = moduleId;
	}

	public int getTriggerFieldId()
	{
		return triggerFieldId;
	}

	public void setTriggerFieldId(int fieldId)
	{
		this.triggerFieldId = fieldId;
	}

	public int getTriggerThresholdOver()
	{
		return triggerThresholdOver;
	}

	public void setTriggerThresholdOver(int thresholdOver)
	{
		this.triggerThresholdOver = thresholdOver;
	}

	public int getTriggerThresholdUnder()
	{
		return triggerThresholdUnder;
	}

	public void setTriggerThresholdUnder(int thresholdUnder)
	{
		this.triggerThresholdUnder = thresholdUnder;
	}

	public boolean isTriggerState()
	{
		return triggerState;
	}

	public void setTriggerState(boolean state)
	{
		this.triggerState = state;
	}


	public Logic getTriggerLogic()
	{
		return triggerLogic;
	}

	public void setTriggerLogic(Logic logics)
	{
		this.triggerLogic = logics;
	}

	@Override public String toString()
	{
		return "Trigger{" +
				"triggerId=" + triggerId +
				", triggerModuleId=" + triggerModuleId +
				", triggerFieldId=" + triggerFieldId +
				", triggerThresholdOver=" + triggerThresholdOver +
				", triggerThresholdUnder=" + triggerThresholdUnder +
				", triggerState=" + triggerState +
				'}';
	}
}
