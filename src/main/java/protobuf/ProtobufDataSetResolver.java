package protobuf;

import client.RaspberryLifeClient;
import data.Config;
import data.model.Module;
import system.service.DataBaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufDataSetResolver {

    public static final String DEBUG_TAG = ProtobufDataSetResolver.class.getSimpleName();

    private RaspberryLifeClient client;

    public void resolve(RaspberryLifeClient client, RblProto.RBLMessage message){
        this.client = client;
        for(RblProto.RBLMessage.DataSet d: message.getDataSetList()){
            switch (d.getCrudType()){
                case CREATE:
                    break;
                case RETRIEVE:
                    retrieveDataSet(d);
                    break;
                case UPDATE:
                    break;
                case DELETE:
                    break;
            }
        }
    }

    private void retrieveDataSet(RblProto.RBLMessage.DataSet d) {
        switch (d.getDataType()){
            case MODULE_LIST:
                sendModuleList();
                break;
            case MODULE_DATA:
                break;
            case SYSTEM_DATA:
                break;
            case CLIENT_LIST:
                break;
            case CLIENT_DATA:
                break;
        }
    }

    private void sendModuleList(){
        List l = DataBaseService.getInstance().readAll(DataBaseService.DataType.MODULE);
        List<RblProto.RBLMessage.Data> dl = new ArrayList<RblProto.RBLMessage.Data>();
        for(Object o : l){
            int id = ((Module) o).getId();
            String name = ((Module) o).getName();
            RblProto.RBLMessage.Actuator a = ProtoFactory.buildActuator(RblProto.RBLMessage.ActuatorType.MODULE, id ,name).build();
            dl.add(ProtoFactory.buildDataMessage(a,null,0,0f,null).build());
        }
        RblProto.RBLMessage.DataSet ds = ProtoFactory.buildDataSet(
                RblProto.RBLMessage.CrudType.RETRIEVE,
                RblProto.RBLMessage.DataType.MODULE_LIST,
                null,
                0,
                null, dl
        );
        List<RblProto.RBLMessage.DataSet> dsl = new ArrayList<RblProto.RBLMessage.DataSet>();
        dsl.add(ds);
        client.sendMessage(ProtoFactory.buildDataSetMessage(
                Config.getConf().getString("server.id"),
                RblProto.RBLMessage.MessageFlag.RESPONSE,
                dsl
        ));
    }


}
