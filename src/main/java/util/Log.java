package util;

import data.Config;

import java.util.Calendar;

/**
 * Created by Peter Mösenthin.
 *
 * The log handles any console or logging output and will later be used to write log files.
 */
public class Log {

    private static final String DEBUG_TAG = Log.class.getSimpleName();

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

    public static void add(String tag,String message, boolean showInConsole){
        if(showInConsole){
            add(tag, message);
        }else{
            //TODO write only to logfile
        }
    }

    public static void add(String tag, String message, Exception e){
        message += " Exception: "
                + e.getClass().getSimpleName() + " - "
                + e.getMessage();
        add(tag, message);
    }


    public static void addClean(String message){
        printToConsole(message);
    }

    public static void printLogHeader(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        addClean("########################################");
        addClean("# RaspberryLife server                 #");
        addClean("# " + c.getTime().toString() + "         #");
        addClean("########################################");
    }


    private static void printToConsole(String text){
        System.out.println(text);
    }
}
