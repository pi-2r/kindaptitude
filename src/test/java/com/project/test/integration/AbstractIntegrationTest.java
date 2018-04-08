package com.project.test.integration;

import com.project.backend.persistence.domain.backend.Plan;
import com.project.backend.persistence.domain.backend.Role;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.domain.backend.UserRole;
import com.project.backend.persistence.repositories.PlanRepository;
import com.project.backend.persistence.repositories.RoleRepository;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.backend.service.EmailValidator;
import com.project.backend.service.UserService;
import com.project.backend.service.CaptchaService;
import com.project.enums.PlansEnum;
import com.project.enums.RolesEnum;
import com.project.utils.UserUtils;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zen on 01/05/17.
 */
public abstract class AbstractIntegrationTest {

    @Autowired
    protected PlanRepository planRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected EmailValidator emailValidator;

    @Autowired
    protected CaptchaService captchaService;

    @Rule
    public TestName testName = new TestName();

    //-----------------> Private methods
    protected Plan createPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }

    protected Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    protected User createUser(String userEmail) {

        Plan basicPlan = createPlan(PlansEnum.USER);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser(userEmail);
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.USER);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser = userRepository.save(basicUser);
        return basicUser;
    }

}
