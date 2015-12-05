package com.delirium.finapp.users.service;

import com.delirium.finapp.infra.common.auth.domain.AbstractCurrentUser;
import com.delirium.finapp.users.domain.User;

import java.util.List;

public interface UserService {

    List<User> findUsers();

    AbstractCurrentUser findCurrentUser();

    User findUser(Long id);

    User findUserByLogin(String login);

    User createUser(User user, Long accountId);

    User updateUser(User user);

    void deleteUser(Long id);

    String getAdminLogin();

}
