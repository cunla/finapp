package com.delirium.finapp.users.service;

import com.delirium.finapp.exceptions.UserCreationException;
import com.delirium.finapp.users.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void setCurrentUser(User user);

    List<User> findUsers();

    List<User> findUsers(String query);

    User findCurrentUser();

    User findUser(Long id);

    User findUserByEmail(String email);

    User createUser(User user) throws UserCreationException;

    User updateUser(User user) throws UserCreationException;

    void deleteUser(Long id);

    String getAdminLogin();

    User createFacebookUser(User user, Long accountId);

    void updateLastGroupId(User user, Long groupId);
}
