package com.delirium.finapp.repo;

import com.delirium.finapp.client.FinappClientFactory;
import com.delirium.finapp.client.FinappService;
import com.delirium.finapp.users.domain.User;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Daniel Moran on 1/14/2015.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@ActiveProfiles(value = Constants.SPRING_PROFILE_DEVELOPMENT)
//@WebAppConfiguration
//@IntegrationTest
//@Transactional
public class TestUserGroupOps {

    private FinappService client = FinappClientFactory.createService();

    @Test
    public void adminLoginLogout() throws IOException {
        client.login("admin@system", "123");
        User user = client.currentUser();
        assertEquals("admin@system", user.getUsername());
        client.logout();
    }

}
