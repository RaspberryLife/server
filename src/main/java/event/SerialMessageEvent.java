package event;

import util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageEvent {

    public static final String DEBUG_TAG = SerialMessageEvent.class.getSimpleName();

    public String rawContent;

    public int moduleType;
    public int moduleId;
    public int messageSeq;
    public int instructionId;
    public List<String> parameters;

    public SerialMessageEvent(String content){
        this.rawContent = content;
    }

    public void populateBase(){
        parameters = new ArrayList<String>();
        try {
            String[] split = rawContent.split(":");
            moduleType = Integer.parseInt(split[0]);
            moduleId = Integer.parseInt(split[1]);
            messageSeq = Integer.parseInt(split[2]);
            instructionId = Integer.parseInt(split[3]);

            for(int i = 4; i < split.length; i++){
                parameters.add(split[i]);
            }
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Unable to populate serial message", e);
        }
    }

}
