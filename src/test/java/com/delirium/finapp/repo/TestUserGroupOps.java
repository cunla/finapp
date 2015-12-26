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
        String admin = "admin@system";
        String password = "123";
        client.login(admin, password);
        User user = client.currentUser();
        assertEquals(admin, user.getUsername());
        client.logout(admin);
    }

    @Test
    public void registerLoginLogout() {
        String email = "style@cs.technion.ac.il";
        String password = "1111";
        User user = client.register(email, password);
        assertEquals(email, user.getEmail());
    }
}
