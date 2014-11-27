package message;

import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import message.events.ModuleInstruction;
import message.events.SerialMessage;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import system.EventBusService;
import util.Config;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

    @Subscribe
    public void handleSerialMessage(SerialMessage message) {
        Log.add(DEBUG_TAG, "Received message: " + message.rawContent);
        message.populateSelf();
        switch (message.instructionId){
            case 99:
                broadcastDebug(message);
                break;
        }

    }

    private void broadcastDebug(SerialMessage message) {
        Log.add(DEBUG_TAG, "Received serial debug " + message.rawContent);
        ClientHandler.broadcastMessage(
                ProtoFactory.buildPlainTextMessage(
                        Config.getConf().getString("server.id"),
                        RblProto.RBLMessage.MessageFlag.RESPONSE,
                        "Serial connector received message: " +
                                message.rawContent
                ));
        ModuleInstruction mi = new ModuleInstruction();
        mi.modelType = message.moduleType;
        mi.moduleId = message.moduleId;
        mi.instructionId = message.instructionId;
        mi.params = message.params;
        EventBusService.post(mi);
    }



}
