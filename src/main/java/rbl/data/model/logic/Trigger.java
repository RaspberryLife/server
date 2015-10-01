package rbl.data.model.logic;

import rbl.data.model.Module;

import javax.persistence.*;

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

	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Logic logic;

	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Module module;

	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Condition condition;

	public Trigger()
	{
	}

	public Trigger(Logic logic, Module module, Condition condition)
	{
		this.logic = logic;
		this.module = module;
		this.condition = condition;
	}

	public Trigger(Module module, Condition condition)
	{
		this.module = module;
		this.condition = condition;
	}

	public int getTrigger_id()
	{
		return trigger_id;
	}

	public void setTrigger_id(int trigger_id)
	{
		this.trigger_id = trigger_id;
	}

	public Logic getLogic()
	{
		return logic;
	}

	public void setLogic(Logic logic)
	{
		this.logic = logic;
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

	@Override public String toString()
	{
		return "Trigger{" +
				"trigger_id=" + trigger_id +
				", logic=" + logic +
				", module=" + module +
				", condition=" + condition +
				'}';
	}
}
