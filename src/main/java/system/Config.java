package system;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import util.Log;

import java.io.*;
import java.net.URLDecoder;

/**
 * Created by Peter Mösenthin.
 *
 * Config class to hold all sorts of configuration for easy access.
 */
public class Config {

    private static XMLConfiguration mConfiguration;
    public static final String DEBUG_TAG = Config.class.getSimpleName();

    public static int DEBUG_LEVEL = 1;


    public static void dumpConfig(){
        String configName =  "rbl_config_ext.xml";
        String path = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] pathSplit = path.split("/");
        Log.add(DEBUG_TAG, "Split 0 " + pathSplit[0]);
        String ext_path = "";
        for(int i = 1; i <pathSplit.length -1; i++){
            ext_path += pathSplit[i] + "/";
        }
        ext_path += configName;

        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(ext_path, "UTF-8");
            Log.add(DEBUG_TAG, "Writing external config: " + decodedPath);
        } catch (UnsupportedEncodingException e) {
            Log.add(DEBUG_TAG, "Could not create path for external configuration.");
            return;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(mConfiguration.getFile());
        } catch (FileNotFoundException e) {
            Log.add(DEBUG_TAG, "Could read configuration file. " + e);
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(fis));

        FileWriter fstream = null;
        try {
            fstream = new FileWriter(ext_path, true);
        } catch (IOException e) {
            Log.add(DEBUG_TAG, "Could open FileWriter." + e);
        }

        BufferedWriter out = new BufferedWriter(fstream);

        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close buffer reader
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close buffer writer
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static XMLConfiguration getConf(){
        if(mConfiguration == null){
            readConfig();
        }
        return mConfiguration;
    }


    public static void readConfig(){
        try {
            mConfiguration = new XMLConfiguration("rbl_config.xml");
            Log.add(DEBUG_TAG, "Configuration loaded.");
        } catch (ConfigurationException e) {
            Log.add(DEBUG_TAG, "Unable to read configuration: " + e);
        }
    }
}
