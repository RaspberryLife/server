package rbl.data.model.logic;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name = "rbl_logic")
public class Logic
{

	public static final String EXECUTION_REQUIREMENT_SINGLE = "single";
	public static final String EXECUTION_REQUIREMENT_MAJORITY = "majority";
	public static final String EXECUTION_REQUIREMENT_ALL = "all";

	private int logic_id;


	private String name;

	private ExecutionFrequency executionFrequency;

	private String executionRequirement;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public Logic()
	{
	}

	@Id
	@GeneratedValue
	@Column(name = "logic_id")
	public int getLogic_id()
	{
		return logic_id;
	}

	public void setLogic_id(int logic_id)
	{
		this.logic_id = logic_id;
	}

	@Column(name = "logic_name")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@OneToOne(mappedBy = "logic", cascade = CascadeType.ALL)
	public ExecutionFrequency getExecutionFrequency()
	{
		return executionFrequency;
	}

	public void setExecutionFrequency(ExecutionFrequency executionFrequency)
	{
		this.executionFrequency = executionFrequency;
	}

	@Column(name = "logic_execution_requirement")
	public String getExecutionRequirement()
	{
		return executionRequirement;
	}

	public void setExecutionRequirement(String executionRequirement)
	{
		this.executionRequirement = executionRequirement;
	}


	@Override public String toString()
	{
		return "Logic{" +
				"logic_id=" + logic_id +
				", name='" + name + '\'' +
				", executionFrequency=" + executionFrequency +
				", executionRequirement='" + executionRequirement + '\'' +
				'}';
	}
}
