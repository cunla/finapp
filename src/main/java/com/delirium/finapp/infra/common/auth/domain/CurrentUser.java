package com.delirium.finapp.infra.common.auth.domain;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.users.domain.User;

/**
 * Created by Daniel Moran on 11/29/2015.
 */
public interface CurrentUser {
    User getUser();

    Group getAccount();
}
