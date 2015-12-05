package com.delirium.finapp.infra.common.accounts.service;

import com.delirium.finapp.groups.domain.Group;

import java.util.List;

public interface AccountsDataService {
    List<Group> findAll();
}
