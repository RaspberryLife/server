package rbl.data.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name = "rbl_logic")
public class Logic
{

	public static final String EXECUTION_REQUIREMENT_SINGLE = "SINGLE";
	public static final String EXECUTION_REQUIREMENT_MAJORITY = "MAJORITY";
	public static final String EXECUTION_REQUIREMENT_ALL = "ALL";

	@Id
	@GeneratedValue
	@Column(name = "logic_id")
	private int logic_id;

	@Column(name = "logic_name")
	private String name;

	@OneToOne(mappedBy = "logic", cascade = CascadeType.ALL)
	private ExecutionFrequency executionFrequency;

	@Column(name = "logic_execution_requirement")
	private String executionRequirement;

	@OneToMany(mappedBy = "logic", cascade = CascadeType.ALL)
	private List<LogicInitiator> logicInitiators = new ArrayList<LogicInitiator>();

	@OneToMany(mappedBy = "logic", cascade = CascadeType.ALL)
	private List<LogicReceiver> logicReceivers = new ArrayList<LogicReceiver>();

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getLogic_id()
	{
		return logic_id;
	}

	public void setLogic_id(int logic_id)
	{
		this.logic_id = logic_id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ExecutionFrequency getExecutionFrequency()
	{
		return executionFrequency;
	}

	public void setExecutionFrequency(ExecutionFrequency executionFrequency)
	{
		this.executionFrequency = executionFrequency;
	}

	public String getExecutionRequirement()
	{
		return executionRequirement;
	}

	public void setExecutionRequirement(String executionRequirement)
	{
		this.executionRequirement = executionRequirement;
	}

	public List<LogicInitiator> getLogicInitiators()
	{
		return logicInitiators;
	}

	public void setLogicInitiators(List<LogicInitiator> logicInitiators)
	{
		this.logicInitiators = logicInitiators;
	}

	public List<LogicReceiver> getLogicReceivers()
	{
		return logicReceivers;
	}

	public void setLogicReceivers(List<LogicReceiver> logicReceivers)
	{
		this.logicReceivers = logicReceivers;
	}
}
