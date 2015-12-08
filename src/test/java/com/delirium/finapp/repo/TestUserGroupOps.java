package com.delirium.finapp.repo;

import com.delirium.finapp.Application;
import com.delirium.finapp.client.FinappClientFactory;
import com.delirium.finapp.client.FinappService;
import com.delirium.finapp.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Daniel Moran on 1/14/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles(value = Constants.SPRING_PROFILE_DEVELOPMENT)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class TestUserGroupOps {

    private FinappService client = FinappClientFactory.createClient();

    @Test
    public void adminLoginLogout() {
        client.login("admin@system", "123");
        client.logout();
    }
}
