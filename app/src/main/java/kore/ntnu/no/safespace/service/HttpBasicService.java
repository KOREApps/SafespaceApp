package kore.ntnu.no.safespace.service;

import android.util.Base64;

import kore.ntnu.no.safespace.data.UserCredentials;

/**
 * Created by robert on 11/19/17.
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
