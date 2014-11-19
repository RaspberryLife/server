package util;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the Authentication. Currently there really isn't any real
 * authentication
 */
public class NoAuth {
    private static final String DEBUG_TAG = NoAuth.class.getSimpleName();
    private static final String key = "abc12345";

    public static boolean verify(String keyToVerify){
        Log.add(DEBUG_TAG, "Verifying key");
        return key.equals(keyToVerify);
    }
}
