package util;

import data.model.User;
import system.service.DataBaseService;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the Authentication. Currently there isn't any real
 * authentication
 */
public class NoAuth {
    private static final String DEBUG_TAG = NoAuth.class.getSimpleName();
    private static final String key = "abc12345";

    public static boolean verify(String keyToVerify){
        Log.add(DEBUG_TAG, "Verifying key");
        return key.equals(keyToVerify);
    }


    public static boolean verify(User user){
        User u = (User) DataBaseService.getInstance().readId(DataBaseService.DataType.USER,
                user.getId()).get(0);
        String db_password,db_email;
        db_email = u.getEmail();
        db_password = u.getPassword();
        return db_email.equals(user.getEmail()) && db_password.equals(user.getPassword());
    }


}
