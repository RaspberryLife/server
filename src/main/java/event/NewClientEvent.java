package event;

import client.RaspberryLifeClient;

/**
 * Created by Peter Mösenthin.
 */
public class NewClientEvent {

    public static final int TYPE_WEB_SOCKET = 0;
    public static final int TYPE_JAVA_SOCKET = 1;

    private int type;

    private RaspberryLifeClient client;

    public NewClientEvent(int type, RaspberryLifeClient client) {
        this.type = type;
        this.client = client;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RaspberryLifeClient getClient() {
        return client;
    }

    public void setClient(RaspberryLifeClient client) {
        this.client = client;
    }
}
