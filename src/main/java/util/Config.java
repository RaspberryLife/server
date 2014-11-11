package util;

/**
 * Created by Peter Mösenthin.
 *
 * Config class to hold all sorts of configuration for easy access.
 */
public class Config {
    public static final String VERSION_ID = "v0.5";
    public static final String SERVER_ID = "rbh_server_" + VERSION_ID;

    public static final int JAVA_SOCKET_PORT = 6666;
    public static final int WEBSOCKET_PORT = 6680;
    public static final int DEBUG_LEVEL = 1;

    public static final String MYSQL_HOST= "jdbc:mysql://localhost:3306/";
    public static final String MYSQL_DBNAME = "testSerial";
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PASSWORD = "";
}
