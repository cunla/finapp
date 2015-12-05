package com.delirium.finapp.infra.common.auth.service;

import com.delirium.finapp.infra.common.auth.domain.AbstractCurrentUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CurrentUserDetailsService extends UserDetailsService {


    AbstractCurrentUser loadUserByUsername(String userName)
        throws UsernameNotFoundException;

}
