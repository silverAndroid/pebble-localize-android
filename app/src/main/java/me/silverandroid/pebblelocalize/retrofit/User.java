package me.silverandroid.pebblelocalize.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rushil Perera on 4/3/2016.
 */
public class User {

    private String _id;
    private String username;
    private String name;
    @SerializedName("response")
    private String response;

    public User(String _id) {
        this._id = _id;
    }

    public User(String _id, String username, String name) {
        this._id = _id;
        this.username = username;
        this.name = name;
    }

    public String getResponse() {
        return response;
    }
}
