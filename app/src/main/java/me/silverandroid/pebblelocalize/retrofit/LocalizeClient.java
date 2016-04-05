package me.silverandroid.pebblelocalize.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Rushil Perera on 4/2/2016.
 */
public interface LocalizeClient {

    //TODO: Figure out returned fields and store them in object
    @FormUrlEncoded
    @POST("/user/create")
    Call<User> register(@Field("name") String name, @Field("username") String username, @Field
            ("pword") String password);

    @FormUrlEncoded
    @POST("/group/create")
    Call<Group> createGroup(@Field("masterId") String masterID, @Field("groupName") String
            groupName, @Field("destLat") float destinationLatitude, @Field("destLng") float
                                              destinationLongitude);

    @FormUrlEncoded
    @PATCH("/group/join")
    Call<Group> joinGroup(@Field("userId") String userID, @Field("groupId") int groupID);

    @FormUrlEncoded
    @DELETE("/group/leave")
    Call<Object> leaveGroup(@Field("userId") String userID);

    @FormUrlEncoded
    @DELETE("/user/leave")
    Call<Object> deleteAccount(@Field("userId") String userID);

    /**
     * Make sure to hash old password and new password before sending
     */
    @FormUrlEncoded
    @PATCH("/user/changePword")
    Call<Object> changePassword(@Field("userId") String userID, @Field("oPword") String
            oldPassword, @Field("nPword") String newPassword);

    @GET("/allUsersInfo")
    Call<List<User>> getUsersInfo(@Field("inputName") String usernameOrName);

    @GET("/usersInfo/{from}/{to}")
    Call<List<User>> getUsersInfoLimit(@Field("inputName") String usernameOrName, @Path("from") int
            from, @Path("to") int to);
}
