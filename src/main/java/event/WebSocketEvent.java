package event;

import org.webbitserver.WebSocketConnection;
import protobuf.RblProto;

/**
 * Created by Peter Mösenthin.
 */
public class WebSocketEvent {

    public enum Type {
        TYPE_CONNECTION_OPEN,
        TYPE_CONNECTION_CLOSE,
        TYPE_MESSAGE
    }


    private RblProto.RBLMessage message;
    private Type type;
    private WebSocketConnection connection;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RblProto.RBLMessage getMessage() {
        return message;
    }

    public void setMessage(RblProto.RBLMessage message) {
        this.message = message;
    }

    public WebSocketConnection getConnection() {
        return connection;
    }

    public void setConnection(WebSocketConnection connection) {
        this.connection = connection;
    }
}
