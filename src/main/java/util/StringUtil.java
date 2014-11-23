package util;


/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the Authentication. Currently there really isn't any real
 * authentication
 */
public class StringUtil {

    public static String getZeroPadded(String content, int maxLength, boolean padLeft){
        int paddingLeft = maxLength - content.length();
        String padded = "";
        for(int i = 0; i < paddingLeft; i++){
            padded += "0";
        }
        if(padLeft){
            padded += content;
        } else{
            padded = content + padded;
        }
        return padded;
    }
}
