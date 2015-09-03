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
	private int condition_id;

	@Column(name = "field_id")
	private int field_id;

	@Column(name = "threshold_over")
	private int threshold_over;

	@Column(name = "threshold_under")
	private int threshold_under;

	@Column(name = "state")
	private boolean state;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private LogicInitiator logic_initiator;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getCondition_id()
	{
		return condition_id;
	}

	public int getField_id()
	{
		return field_id;
	}

	public void setField_id(int field_id)
	{
		this.field_id = field_id;
	}

	public int getThreshold_over()
	{
		return threshold_over;
	}

	public void setThreshold_over(int threshold_over)
	{
		this.threshold_over = threshold_over;
	}

	public int getThreshold_under()
	{
		return threshold_under;
	}

	public boolean isState()
	{
		return state;
	}

	public void setState(boolean state)
	{
		this.state = state;
	}

	public void setThreshold_under(int threshold_under)
	{
		this.threshold_under = threshold_under;
	}

	public LogicInitiator getLogic_initiator()
	{
		return logic_initiator;
	}

	public void setLogic_initiator(LogicInitiator logic_initiator)
	{
		this.logic_initiator = logic_initiator;
	}
}
