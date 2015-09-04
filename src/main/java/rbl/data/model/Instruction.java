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
	private int instructionId;

	@Column(name = "field_id")
	private int fieldId;

	@Column(name = "instruction_parameters")
	private String parameters;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private LogicInitiator logicInitiator;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getInstructionId()
	{
		return instructionId;
	}

	public int getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(int fieldId)
	{
		this.fieldId = fieldId;
	}

	public String getParameters()
	{
		return parameters;
	}

	public void setParameters(String parameters)
	{
		this.parameters = parameters;
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
