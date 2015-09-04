package rbl.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_actuator_table")
public class Actuator
{

	public static final String TYPE_SYSTEM = "SYSTEM";
	public static final String TYPE_MODULE = "MODULE";
	public static final String TYPE_CLIENT = "CLIENT";

	@Id
	@GeneratedValue
	@Column(name = "actuator_id")
	private int actuator_id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@OneToMany(mappedBy = "actuator", cascade = CascadeType.ALL)
	private Set<LogicInitiator> logicInitiators;

	@OneToMany(mappedBy = "actuator", cascade = CascadeType.ALL)
	private Set<LogicReceiver> logicReceivers;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getActuator_id()
	{
		return actuator_id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Set<LogicInitiator> getLogicInitiators()
	{
		return logicInitiators;
	}

	public void setLogicInitiators(Set<LogicInitiator> logicInitiators)
	{
		this.logicInitiators = logicInitiators;
	}

	public Set<LogicReceiver> getLogicReceivers()
	{
		return logicReceivers;
	}

	public void setLogicReceivers(Set<LogicReceiver> logicReceivers)
	{
		this.logicReceivers = logicReceivers;
	}
}
