package server;

/**
 * Created by Peter Mösenthin.
 *
 * Calss to handle the Authentication. Currently there really isnt any real
 * authentication
 */
public class NoAuth {
    private static final String DEBUG_TAG = "SimpleAuth";
    private static final String key = "abc12345";

    public static boolean verify(String keyToVerify){
        return key.equals(keyToVerify);
    }
}
