package rbl.data.model;

import rbl.data.model.logic.Trigger;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rbl_module")
public class Module
{

	public static final String TYPE_OUTLET = "OUTLET";
	public static final String TYPE_PIR = "PIR";
	public static final String TYPE_REED = "REED";
	public static final String TYPE_TEMP = "TEMP";
	public static final String TYPE_LUMINOSITY = "LUMINOSITY";
	public static final String TYPE_RELAY = "RELAY";
	public static final String TYPE_PIR_AND_RELAY = "PIR_AND_RELAY";
	public static final String TYPE_STATUS_MONITOR = "STATUS_MONITOR";

	@Id
	@GeneratedValue
	@Column(name = "module_id")
	private int id;

	@Column(name = "module_type")
	private String type;

	@Column(name = "serial_address")
	private String serialAddress;

	@Column(name = "name")
	private String name;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public int getId()
	{
		return id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSerialAddress()
	{
		return serialAddress;
	}

	public void setSerialAddress(String serialAddress)
	{
		this.serialAddress = serialAddress;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override public String toString()
	{
		return "Module{" +
				"id=" + id +
				", type='" + type + '\'' +
				", serialAddress='" + serialAddress + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
