package message.events;

import util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessage {

    public static final String DEBUG_TAG = SerialMessage.class.getSimpleName();

    public String rawContent;

    public int moduleType;
    public int moduleId;
    public int messageSeq;
    public int instructionId;
    public List<Integer> params;

    public SerialMessage(String content){
        this.rawContent = content;
    }

    public void populateSelf(){
        params = new ArrayList<Integer>();
        try {
            String[] split = rawContent.split(":");
            moduleType = Integer.parseInt(split[0]);
            moduleId = Integer.parseInt(split[1]);
            messageSeq = Integer.parseInt(split[2]);
            instructionId = Integer.parseInt(split[3]);

            for(int i = 4; i < split.length; i++){
                params.add(Integer.parseInt(split[i]));
            }
        } catch (IndexOutOfBoundsException e){
            Log.add(DEBUG_TAG, "Unable to populate serial message", e);
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Unable to populate serial message", e);
        }
    }

}
