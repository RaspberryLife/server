package protobuf;

import client.RaspberryLifeClient;
import data.model.User;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufUserResolver {

    public static final String DEBUG_TAG = ProtobufUserResolver.class.getSimpleName();

    private RaspberryLifeClient client;

    public void resolve(RaspberryLifeClient client, RblProto.RBLMessage message){
        this.client = client;
        for(RblProto.RBLMessage.User user : message.getUserList()){
            switch (user.getCrudType()){
                case CREATE:
                    createUser(user);
                    break;
                case RETRIEVE:
                    retrieveUser(user);
                    break;
                case UPDATE:
                    updateUser(user);
                    break;
                case DELETE:
                    deleteUser(user);
                    break;
            }
        }
    }

    private void createUser(RblProto.RBLMessage.User user){

    }

    private void retrieveUser(RblProto.RBLMessage.User user){

    }

    private void updateUser(RblProto.RBLMessage.User user){

    }

    private void deleteUser(RblProto.RBLMessage.User user){

    }

    //----------------------------------------------------------------------------------------------
    //                                      MAPPINGS
    //----------------------------------------------------------------------------------------------

    public static User mapModelUser(RblProto.RBLMessage.User user){
        User u = new User();
        if(user.hasId()){
            u.setId(user.getId());
        }
        if(user.hasEmail()){
            u.setEmail(user.getEmail());
        }
        if(user.hasPassword()){
            u.setPassword(user.getPassword());
        }
        return u;
    }

    public static RblProto.RBLMessage.User mapProtobufUser(User user){
        return ProtoFactory.buildUser(user.getId(),user.getEmail(), user.getPassword()).build();
    }


}
