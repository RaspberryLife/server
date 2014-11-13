package util;

/**
 * Created by Peter Mösenthin.
 *
 * Config class to hold all sorts of configuration for easy access.
 */
public class Config {
    public static final String SERVER_ID = "rbh_server_master";

    public static int JAVA_SOCKET_PORT = 6666;
    public static int WEBSOCKET_PORT = 6680;
    public static int DEBUG_LEVEL = 1;

    public static String MYSQL_HOST= "localhost";
    public static String MYSQL_PORT= "3306";
    public static String MYSQL_DB_NAME = "rbl_data";
    public static String MYSQL_USER = "root";
    public static String MYSQL_PASSWORD = "";

    public static int SERIAL_PORT_BAUDRATE = 38400;
    public static int SERIAL_PORT_DATA_BITS = 8;
    public static int SERIAL_PORT_STOP_BITS = 1;
    public static int SERIAL_PORT_PARITY = 0;
    public static int SERIAL_MESSAGE_BYTE_LENGTH = 32;
}
