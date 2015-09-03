package rbl.extension.fablab.response;

import java.util.ArrayList;
import java.util.List;

public class FabLabStatus
{
	List<WindowModule> moduleList = new ArrayList<>();

	public FabLabStatus(List<WindowModule> moduleList)
	{
		this.moduleList = moduleList;
	}

	public List<WindowModule> getModuleList()
	{
		return moduleList;
	}

	public void setModuleList(List<WindowModule> moduleList)
	{
		this.moduleList = moduleList;
	}
}
