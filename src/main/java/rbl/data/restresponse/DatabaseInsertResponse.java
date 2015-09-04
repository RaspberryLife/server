package rbl.data.restresponse;

public class DatabaseInsertResponse
{
	boolean insertOK;
	String message;

	public DatabaseInsertResponse(boolean insertOK, String message)
	{
		this.insertOK = insertOK;
		this.message = message;
	}

	public boolean isInsertOK()
	{
		return insertOK;
	}

	public void setInsertOK(boolean insertOK)
	{
		this.insertOK = insertOK;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
