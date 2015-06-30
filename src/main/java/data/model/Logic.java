package data.model;

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
	private ExecutionFrequency execution_frequency;

	@Column(name = "logic_execution_requirement")
	private String execution_requirement;

	@OneToMany(mappedBy = "logic", cascade = CascadeType.ALL)
	private List<LogicInitiator> logic_initiator = new ArrayList<LogicInitiator>();

	@OneToMany(mappedBy = "logic", cascade = CascadeType.ALL)
	private List<LogicReceiver> logic_receiver = new ArrayList<LogicReceiver>();

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

	public ExecutionFrequency getExecution_frequency()
	{
		return execution_frequency;
	}

	public void setExecution_frequency(ExecutionFrequency execution_frequency)
	{
		this.execution_frequency = execution_frequency;
	}

	public String getExecution_requirement()
	{
		return execution_requirement;
	}

	public void setExecution_requirement(String execution_requirement)
	{
		this.execution_requirement = execution_requirement;
	}

	public List<LogicInitiator> getLogic_initiator()
	{
		return logic_initiator;
	}

	public void setLogic_initiator(List<LogicInitiator> logic_initiator)
	{
		this.logic_initiator = logic_initiator;
	}

	public List<LogicReceiver> getLogic_receiver()
	{
		return logic_receiver;
	}

	public void setLogic_receiver(List<LogicReceiver> logic_receiver)
	{
		this.logic_receiver = logic_receiver;
	}
}
