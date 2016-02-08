package com.delirium.finapp.users.domain;

/**
 * Created by morand3 on 2/8/2016.
 */
public class SimpleUser {
    private final String email;
    private final String name;
    private final String avatar;
    private final Long id;

    public SimpleUser(User user){
        this.id=user.getId();
        this.name=user.getName();
        this.email=user.getEmail();
        this.avatar=user.getAvatar();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Long getId() {
        return id;
    }
}
