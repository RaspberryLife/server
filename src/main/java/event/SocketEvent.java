package event;

import java.net.Socket;

/**
 * Created by Peter Mösenthin.
 */
public class SocketEvent {

    private Type type;
    private Socket socket;

    public enum Type {
        SOCKET_ACCEPT
    }

    public SocketEvent(Type type) {
        this.type = type;
    }

    public SocketEvent(Type type, Socket socket) {
        this.type = type;
        this.socket = socket;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
