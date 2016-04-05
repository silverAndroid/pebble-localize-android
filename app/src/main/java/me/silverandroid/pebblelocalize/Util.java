package me.silverandroid.pebblelocalize;

import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Rushil Perera on 4/3/2016.
 */
public class Util {

    private static final String TAG = "Util";

    public static String hashPassword(String password) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA256");
            byte[] hashedPasswordBytes = digester.digest(password.getBytes());
            return Arrays.toString(hashedPasswordBytes);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "hashPassword: Algorithm does not exist", e);
        }
        return null;
    }
}
