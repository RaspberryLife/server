package rbl.data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_condition")
public class Condition
{

	@Id
	@GeneratedValue
	@Column(name = "condition_id")
	private int conditionId;

	@Column(name = "field_id")
	private int fieldId;

	@Column(name = "threshold_over")
	private int thresholdOver;

	@Column(name = "threshold_under")
	private int thresholdUnder;

	@Column(name = "state")
	private boolean state;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private LogicInitiator logicInitiator;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getConditionId()
	{
		return conditionId;
	}

	public int getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(int fieldId)
	{
		this.fieldId = fieldId;
	}

	public int getThresholdOver()
	{
		return thresholdOver;
	}

	public void setThresholdOver(int thresholdOver)
	{
		this.thresholdOver = thresholdOver;
	}

	public int getThresholdUnder()
	{
		return thresholdUnder;
	}

	public boolean isState()
	{
		return state;
	}

	public void setState(boolean state)
	{
		this.state = state;
	}

	public void setThresholdUnder(int thresholdUnder)
	{
		this.thresholdUnder = thresholdUnder;
	}

	public LogicInitiator getLogicInitiator()
	{
		return logicInitiator;
	}

	public void setLogicInitiator(LogicInitiator logicInitiator)
	{
		this.logicInitiator = logicInitiator;
	}
}
