package me.silverandroid.pebblelocalize.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rushil Perera on 4/3/2016.
 */
public class Group {

    private int _id; //Now based on master user ID
    private String groupName; //Name of group, not hashed group ID
    private float destLat;
    private float destLong;
    @SerializedName("response")
    private String response;

    public Group(int id) {
        _id = id;
    }

    public Group(int _id, String groupName, float destLat, float destLong) {
        this._id = _id;
        this.groupName = groupName;
        this.destLat = destLat;
        this.destLong = destLong;
    }

    public String getResponse() {
        return response;
    }

    public float getDestLat() {
        return destLat;
    }

    public float getDestLong() {
        return destLong;
    }
}
