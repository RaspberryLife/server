package rbl.data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rblModule")
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
	@Column(name = "moduleId")
	private int moduleId;

	@Column(name = "moduleType")
	private String moduleType;

	@Column(name = "serialAddress")
	private String serialAddress;

	@Column(name = "moduleName")
	private String moduleName;

	@Column(name = "moduleRoom")
	private String moduleRoom;

	@Column(name = "moduleState")
	private String moduleState;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public Module()
	{
	}

	public Module(String moduleName, String moduleRoom)
	{
		this.moduleName = moduleName;
		this.moduleRoom = moduleRoom;
	}

	public int getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(int id)
	{
		this.moduleId = id;
	}

	public String getModuleType()
	{
		return moduleType;
	}

	public void setModuleType(String type)
	{
		this.moduleType = type;
	}

	public String getSerialAddress()
	{
		return serialAddress;
	}

	public void setSerialAddress(String serialAddress)
	{
		this.serialAddress = serialAddress;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String name)
	{
		this.moduleName = name;
	}

	public String getModuleRoom()
	{
		return moduleRoom;
	}

	public void setModuleRoom(String room)
	{
		this.moduleRoom = room;
	}

	public String getModuleState()
	{
		return moduleState;
	}

	public void setModuleState(String state)
	{
		this.moduleState = state;
	}

	@Override public String toString()
	{
		return "Module{" +
				"moduleId=" + moduleId +
				", moduleType='" + moduleType + '\'' +
				", serialAddress='" + serialAddress + '\'' +
				", moduleName='" + moduleName + '\'' +
				", moduleRoom='" + moduleRoom + '\'' +
				", moduleState='" + moduleState + '\'' +
				'}';
	}
}
