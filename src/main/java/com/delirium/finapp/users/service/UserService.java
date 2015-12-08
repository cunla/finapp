package com.delirium.finapp.users.service;

import com.delirium.finapp.users.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findUsers();

    List<User> findUsers(String query);

    User findCurrentUser();

    User findUser(Long id);

    User findUserByEmail(String email);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    String getAdminLogin();

    User createFacebookUser(User user, Long accountId);
}
