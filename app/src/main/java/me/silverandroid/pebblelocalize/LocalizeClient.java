package me.silverandroid.pebblelocalize;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Rushil Perera on 4/2/2016.
 */
public interface LocalizeClient {

    //TODO: Figure out returned fields and store them in object
    @FormUrlEncoded
    @POST("/users/join")
    Call<Object> register(@Field("name") String name, @Field("username") String username, @Field("pword")
    String password);

//    @FormUrlEncoded
//    @POST("/users/login")
//    Call<Object> login(@Field("username") String username, @Field("pword") String password);
}
