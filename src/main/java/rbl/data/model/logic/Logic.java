package rbl.data.model.logic;

import javax.persistence.*;
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
	private Set<Trigger> triggers;

	@OneToMany(mappedBy = "logic", cascade = CascadeType.ALL)
	private Set<Action> actions;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public Logic()
	{
	}

	public Logic(String name, ExecutionFrequency executionFrequency, String executionRequirement,
			Set<Trigger> triggers, Set<Action> actions)
	{
		this.name = name;
		this.executionFrequency = executionFrequency;
		this.executionRequirement = executionRequirement;
		this.triggers = triggers;
		this.actions = actions;
	}

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

	public Set<Trigger> getTriggers()
	{
		return triggers;
	}

	public void setTriggers(Set<Trigger> triggers)
	{
		this.triggers = triggers;
	}

	public Set<Action> getActions()
	{
		return actions;
	}

	public void setActions(Set<Action> actions)
	{
		this.actions = actions;
	}

	@Override public String toString()
	{
		return "Logic{" +
				"logic_id=" + logic_id +
				", name='" + name + '\'' +
				", executionFrequency=" + executionFrequency +
				", executionRequirement='" + executionRequirement + '\'' +
				", triggers=" + triggers +
				", actions=" + actions +
				'}';
	}
}
