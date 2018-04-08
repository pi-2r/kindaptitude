package com.project.test.integration;

import com.project.DevopsbuddyApplication;
import com.project.backend.persistence.domain.backend.Plan;
import com.project.backend.persistence.domain.backend.Role;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.domain.backend.UserRole;
import com.project.enums.PlansEnum;
import com.project.enums.RolesEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;
import java.util.UUID;

/**
 * Created by zen on 26/04/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {


    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception {
        Plan basicPlan = createPlan(PlansEnum.USER);
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(PlansEnum.USER.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception {

        Role userRole  = createRole(RolesEnum.USER);
        roleRepository.save(userRole);

        Role retrievedRole = roleRepository.findOne(RolesEnum.USER.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void getOwnerByCompanyId()
    {
        Long domaineName = 1L;
        User owner = userService.findMasterGroupByCompanyId(domaineName);
        Assert.assertNotNull(owner);
    }
    @Test
    public void createNewUser() throws Exception {
        String userEmail = testName.getMethodName() + "@gmail.com";
        User basicUser = createUser(userEmail);

        User newlyCreatedUser = userRepository.findOne(basicUser.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.getPlan());
        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
        Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.getUserRoles();
        for (UserRole ur : newlyCreatedUserUserRoles) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }

    }

    @Test
    public void testDeleteUser() throws Exception {
        String userEmail = testName.getMethodName() + "@gmail.com";
        User basicUser = createUser(userEmail);
        userRepository.delete(basicUser.getId());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        User user = createUser(testName.toString());

        User newlyFoundUser = userRepository.findByEmail(user.getEmail());
        Assert.assertNotNull(newlyFoundUser);
        Assert.assertNotNull(newlyFoundUser.getId());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        User user = createUser(testName.toString());
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        String newPassword = UUID.randomUUID().toString();
        userRepository.updateUserPassword(user.getId(), newPassword);
        user = userRepository.findOne(user.getId());
        Assert.assertEquals(newPassword, user.getPassword());
    }
}