package com.delirium.finapp.client;

import com.delirium.finapp.users.domain.User;
import retrofit.http.*;

import java.util.List;

public interface FinappService {
    @FormUrlEncoded
    @POST("/login-action")
    public Void login(@Field("username") String user, @Field("password") String password);

    @POST("/logout-action")
    public Void logout();

    @POST("/users")
    public User createUser(@Body User user);

    @GET("/users/current-user")
    public User currentUser();

    @GET("/users")
    public List<User> queryUsers(@Query("query") String q);

    @GET("/users/{groupId}")
    public List<User> groupUsers(@Path("groupId") Long id);
}
