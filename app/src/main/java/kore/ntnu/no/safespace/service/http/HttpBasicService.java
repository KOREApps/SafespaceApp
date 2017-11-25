package kore.ntnu.no.safespace.service.http;

import android.util.Base64;

import kore.ntnu.no.safespace.data.UserCredentials;

/**
 * Class description..
 *
 * @author Robert
 */
public class HttpBasicService {

    public static String getBasicCredentials(String username, String password){
        final String textToEncode = username + ":" + password;
        return Base64.encodeToString(textToEncode.getBytes(), Base64.NO_WRAP);
    }

    public static String getBasicCredentials(UserCredentials userCredentials) {
        return getBasicCredentials(userCredentials.getUsername(), userCredentials.getPassword());
    }

}
