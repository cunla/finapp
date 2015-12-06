package com.delirium.finapp.infra.sqlplugin.auth.domain;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.infra.common.auth.domain.AbstractCurrentUser;
import com.delirium.finapp.users.domain.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUserSqlImpl extends AbstractCurrentUser {

    private User user;

    public CurrentUserSqlImpl(User user) {
        super(user.getEmail(), user.getPassword(),
            AuthorityUtils.createAuthorityList(user.getPermission()));
        this.user = user;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Group getAccount() {
        return user.getGroup();
    }

	/*@Override
    public List<SystemSettings> getSystemSettings() {
		List<SystemSettings> res = null;
		if(getAccount() != null){
			res = getAccount().getSystemSettings();
		}
		return res;
	}

	@Override
	public List<VmOwnership> getVms() {
		List<VmOwnership> res = null;
		if(getAccount() != null){
			res = getAccount().getVms();
		}
		return res;
	}

	@Override
	public List<AccountConfig> getAccountConfig() {
		List<AccountConfig> res = null;
		if(getAccount() != null){
			res = getAccount().getAccountConfigs();
		}
		return res;
	}*/

}
