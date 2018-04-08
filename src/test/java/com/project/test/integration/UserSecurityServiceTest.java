package com.project.test.integration;

import com.project.DevopsbuddyApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by zen on 30/04/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class UserSecurityServiceTest extends AbstractIntegrationTest{



    private final String validEmail = "pierre.therrode@gmail.com";
    private final String invalidEmail = "pierre.therrodqsmkù";


    @Test
    public void testValidEmail()  {
        Boolean valideEmail = emailValidator.validate(validEmail);
        Assert.assertEquals(valideEmail,true);
    }

    @Test
    public void testInvalidEmail() {
        Boolean valideEmail = emailValidator.validate(invalidEmail);
        Assert.assertEquals(valideEmail,false);

    }


}
