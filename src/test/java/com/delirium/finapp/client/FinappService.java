package com.delirium.finapp.client;

import com.delirium.finapp.users.domain.User;
import com.squareup.okhttp.Response;
import retrofit.http.*;

import java.util.List;

public interface FinappService {

    @POST("/login-action")
    @FormUrlEncoded
    public Response login(@Field(encodeValue = false, value = "username") String user,
                          @Field(encodeValue = false, value = "password") String password);

    @POST("/logout-action")
    @FormUrlEncoded
    public Response logout(@Field(encodeValue = false, value = "username") String user);

    @POST("/register")
    @FormUrlEncoded
    public User register(@Field(encodeValue = false, value = "username") String user,
                         @Field(encodeValue = false, value = "password") String password);

    @GET("/users/current-user")
    public User currentUser();

    @GET("/users")
    public List<User> queryUsers(@Query("query") String q);

    @GET("/users/{groupId}")
    public List<User> groupUsers(@Path("groupId") Long id);
}
