package event;

import client.RaspberryLifeClient;

/**
 * Created by Peter Mösenthin.
 */
public class NewClientEvent
{

	public enum Type
	{
		TYPE_WEB_SOCKET,
		TYPE_JAVA_SOCKET
	}

	private Type type;

	private RaspberryLifeClient client;

	public NewClientEvent(Type type, RaspberryLifeClient client)
	{
		this.type = type;
		this.client = client;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public RaspberryLifeClient getClient()
	{
		return client;
	}

	public void setClient(RaspberryLifeClient client)
	{
		this.client = client;
	}
}
