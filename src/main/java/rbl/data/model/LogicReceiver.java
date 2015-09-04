package rbl.data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_logic_receiver")
public class LogicReceiver
{

	@Id
	@GeneratedValue
	@Column(name = "logic_receiver_id")
	private int logicReceiverId;

	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Logic logic;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "actuator")
	private Actuator actuator;

	@OneToOne(mappedBy = "logic_receiver", cascade = CascadeType.ALL)
	private Instruction instruction;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getLogicReceiverId()
	{
		return logicReceiverId;
	}

	public void setLogicReceiverId(int logicReceiverId)
	{
		this.logicReceiverId = logicReceiverId;
	}

	public Logic getLogic()
	{
		return logic;
	}

	public void setLogic(Logic logic)
	{
		this.logic = logic;
	}

	public Actuator getActuator()
	{
		return actuator;
	}

	public void setActuator(Actuator actuator)
	{
		this.actuator = actuator;
	}

	public Instruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(Instruction instruction)
	{
		this.instruction = instruction;
	}

}
