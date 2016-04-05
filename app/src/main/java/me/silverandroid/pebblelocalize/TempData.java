package me.silverandroid.pebblelocalize;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rushil Perera on 4/3/2016.
 */
public class TempData {

    private static String USER_ID_KEY = "userID";
    private static String LATITUDE_KEY = "latitude";
    private static String LONGITUDE_KEY = "longitude";
    private static TempData instance;
    private Context context;

    private TempData(Context context) {
        this.context = context;
    }

    public static void newInstance(Context context) {
        if (instance == null) {
            instance = new TempData(context);
        }
    }

    public static TempData getInstance() {
        return instance;
    }

    public void saveUserID(String userID) {
        getSharedPreferences().edit().putString(USER_ID_KEY, userID).commit();
    }

    public String getUserID() {
        return getSharedPreferences().getString(USER_ID_KEY, "");
    }

    public boolean isLoggedIn() {
        return getSharedPreferences().contains(USER_ID_KEY);
    }

    public void setCoordinates(float latitude, float longitude) {
        getSharedPreferences().edit().putFloat(LATITUDE_KEY, latitude).apply();
        getSharedPreferences().edit().putFloat(LONGITUDE_KEY, longitude).apply();
    }

    public float getLatitude() {
        return getSharedPreferences().getFloat(LATITUDE_KEY, -91);
    }

    public float getLongitude() {
        return getSharedPreferences().getFloat(LONGITUDE_KEY, -91);
    }

    public boolean isInGroup() {
        return getSharedPreferences().contains(LATITUDE_KEY) && getSharedPreferences().contains
                (LONGITUDE_KEY);
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("Temp Data", Context.MODE_PRIVATE);
    }
}
