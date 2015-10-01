package rbl.data.model.logic;

import rbl.data.model.Module;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_trigger")
public class Trigger
{
	@Id
	@GeneratedValue
	@Column(name = "actuator_id")
	private int trigger_id;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Module module;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Condition condition;

	public Trigger()
	{
	}

	public int getTrigger_id()
	{
		return trigger_id;
	}

	public void setTrigger_id(int trigger_id)
	{
		this.trigger_id = trigger_id;
	}

	public Module getModule()
	{
		return module;
	}

	public void setModule(Module module)
	{
		this.module = module;
	}

	public Condition getCondition()
	{
		return condition;
	}

	public void setCondition(Condition condition)
	{
		this.condition = condition;
	}
}
