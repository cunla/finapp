package com.delirium.finapp.infra.common.auth.domain;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.users.domain.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class AbstractCurrentUser extends org.springframework.security.core.userdetails.User
    implements CurrentUser {

    public AbstractCurrentUser(String username, String password,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public abstract User getUser();

    public abstract Group getAccount();
/*    public abstract List<SystemSettings> getSystemSettings();
    public abstract List<VmOwnership> getVms();
    public abstract List<AccountConfig> getAccountConfig();*/

}
