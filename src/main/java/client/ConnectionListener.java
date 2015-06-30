package client;

/**
 * Created by Peter Mösenthin.
 * <p>
 * This Interface is used by the clients to set connection state changes.
 */
public interface ConnectionListener
{

	void denied(String reason);

	void accepted();

	void closed();
}
