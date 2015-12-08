package com.delirium.finapp.groups.service;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.users.domain.User;

import java.util.List;

public interface GroupService {
    List<Group> findAll();

    List<Group> findAllForUser(User user);

    Group findById(Long id);

    Group create(Group group);

    Group update(Group user);

    void delete(Long id);

    Group findByName(String name);
}
