package com.delirium.finapp.infra.sqlplugin.auth.service.impl;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.AccountService;
import com.delirium.finapp.infra.common.auth.service.CurrentUserDetailsService;
import com.delirium.finapp.infra.sqlplugin.auth.domain.CurrentUserSqlImpl;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsServiceSqlImpl implements CurrentUserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Override
    public CurrentUserSqlImpl loadUserByUsername(String userName)
        throws UsernameNotFoundException {

        User user = null;

        if (isImpersonatedAdmin(userName)) {
            user = userService.findUserByLogin(userService.getAdminLogin());
            Group group = accountService
                .findByName(getAccountName(userName));
            if (group != null) {
                user.setGroup(group);
                user.setEmail(userName);
            } else {
                user = null;
            }
        } else {
            user = userService.findUserByLogin(userName);
        }
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }
        CurrentUserSqlImpl currentUser = new CurrentUserSqlImpl(user);
        return currentUser;
    }

    private boolean isImpersonatedAdmin(String login) {
        boolean res = false;
        String adminLoginPrefix = userService.getAdminLogin().split("@")[0];
        String currLoginPrefix = login.split("@")[0];
        if (adminLoginPrefix.equals(currLoginPrefix)) {
            if (!userService.getAdminLogin().equals(login)) {
                res = true;
            }
        }
        return res;
    }

    private String getAccountName(String login) {
        String res = login.split("@")[1];
        return res;
    }

}
