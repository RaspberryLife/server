package rbl.data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_instruction")
public class Instruction
{

	@Id
	@GeneratedValue
	@Column(name = "instruction_id")
	private int instruction_id;

	@Column(name = "field_id")
	private int field_id;

	@Column(name = "instruction_parameters")
	private String parameters;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private LogicInitiator logic_receiver;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getInstruction_id()
	{
		return instruction_id;
	}

	public int getField_id()
	{
		return field_id;
	}

	public void setField_id(int field_id)
	{
		this.field_id = field_id;
	}

	public String getParameters()
	{
		return parameters;
	}

	public void setParameters(String parameters)
	{
		this.parameters = parameters;
	}

	public LogicInitiator getLogic_receiver()
	{
		return logic_receiver;
	}

	public void setLogic_receiver(LogicInitiator logic_receiver)
	{
		this.logic_receiver = logic_receiver;
	}

}
