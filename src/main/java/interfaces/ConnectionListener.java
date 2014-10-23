package interfaces;

/**
 * Created by Peter Mösenthin.
 *
 * This Interface is used by the clients to set connection state changes.
 */
public interface ConnectionListener {

    void denied(String reason);
    void accepted();
    void closed();
}
