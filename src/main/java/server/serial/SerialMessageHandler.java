package server.serial;

import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import event.message.ModuleInstruction;
import event.message.SerialMessage;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import event.EventBusService;
import system.Config;
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

        mi.setType(message.moduleType);
        mi.setModuleId(message.moduleId);
        mi.setInstructionId(message.instructionId);
        mi.setIntParameters(message.intParameters);
        EventBusService.post(mi);
    }



}
