package com.delirium.finapp.groups.service;

import com.delirium.finapp.groups.domain.Group;

import java.util.List;

public interface AccountService {
    List<Group> findAll();

    Group findById(Long id);

    Group create(Group group, Long systemId);

    Group update(Group user);

    void delete(Long id);

    Group findByName(String name);
}
