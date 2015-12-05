package com.delirium.finapp.infra.sqlplugin.accounts.service.impl;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.repository.AccountRepository;
import com.delirium.finapp.infra.common.accounts.service.AccountsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsDataServiceSqlImpl implements AccountsDataService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Group> findAll() {
        List<Group> groups = accountRepository.findAll();
        return groups;
    }

}
