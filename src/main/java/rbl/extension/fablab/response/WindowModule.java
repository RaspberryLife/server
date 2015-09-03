package rbl.extension.fablab.response;

public class WindowModule
{
	int id;
	int state;

	public WindowModule(int id, int state)
	{
		this.id = id;
		this.state = state;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}
}
