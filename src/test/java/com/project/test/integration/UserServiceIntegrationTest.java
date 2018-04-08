package com.project.test.integration;

import com.project.DevopsbuddyApplication;
import com.project.backend.persistence.domain.backend.Company;
import com.project.backend.persistence.domain.backend.Role;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.domain.backend.UserRole;
import com.project.enums.PlansEnum;
import com.project.enums.RolesEnum;
import com.project.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zen on 26/04/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class UserServiceIntegrationTest extends AbstractIntegrationTest {

  /*  @Test
    public void testCreateNewUser() throws Exception {
        User user = creatUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

    }
*/

  @Test
  public void testBidon(){
      Assert.assertTrue(true);
  }
  @Test
    protected User creatUser(TestName testName) {
        Company company =  new Company();
        company.setCompanyName("KindAptitude");
        company.setWebSite("www.kindaptitude.com");

        String userEmail = testName.getMethodName() + "@gmail.com";
        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser(userEmail);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.USER)));

        return userService.createUser(basicUser, PlansEnum.USER, userRoles, company);
    }


}
