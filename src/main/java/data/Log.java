package data;

import util.Config;

/**
 * Created by Peter Mösenthin.
 *
 * The log handles any console or logging output.
 */
public class Log {

    private static final String DEBUG_TAG = "Log";

    public static void add(String tag,String message){
       switch(Config.DEBUG_LEVEL){
           case 0:
               break;
           case 1:
               printToConsole("[" + tag + "]: " + message);
               break;
           default:
               printToConsole("[" + tag + "]: " + message);
       }
    }


    private static void printToConsole(String text){
        System.out.println(text);
    }
}
