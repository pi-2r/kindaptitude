package com.project.backend.service;

import com.project.backend.persistence.domain.backend.Company;
import com.project.backend.persistence.domain.backend.Plan;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.domain.backend.UserRole;
import com.project.backend.persistence.repositories.CompanyRepository;
import com.project.backend.persistence.repositories.PlanRepository;
import com.project.backend.persistence.repositories.RoleRepository;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.enums.PlansEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zen on 14/05/17.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
public class UserService {

    final String regex = "(?<=@)[a-zA-Z0-9\\.]+(?<=)";
    final Pattern pattern = Pattern.compile(regex);


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CompanyRepository companyRepository;


    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles, Company company) {

        String password = "pierre";
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setBlockedByAdmin(false);

        Plan plan = new Plan(plansEnum);
        // It makes sure the plans exist in the database
        if (!planRepository.exists(plansEnum.getId())) {
            plan = planRepository.save(plan);
        }

        companyRepository.save(company);

        user.setCompany(company);
        user.setPlan(plan);

       /*if(findByCompanyName(user.getEmail())) {
            user.setMasterGroupe(true);
        }else {
            user.setMasterGroupe(false);
        }*/

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

        user.getUserRoles().addAll(userRoles);
        user = userRepository.save(user);

        return user;

    }

    /**
     * Returns a user by username or null if a user could not be found.
     * @param username The username to be found
     * @return A user by username or null if a user could not be found.
     */
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Returns a user for the given email or null if a user could not be found.
     * @param email The email associated to the user to find.
     * @return a user for the given email or null if a user could not be found.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User findById(long id) { return userRepository.findById(id);}

    public User findMasterGroupByCompanyId(Long companyId){return userRepository.findMasterGroupByCompanyId(companyId);}

    public List<User> getAllNewUserByCompany(Long companyId){
        return userRepository.getAllNewUserByCompany(companyId);}
    public void deleteById(long id) {  userRepository.deleteById(id);}

    public Boolean findByCompanyName(String email) {
        boolean master = false;

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        while (matcher.find()) {
            if(userRepository.findByEmailAddress(matcher.group(0).trim().toLowerCase()) ==null) {
                master=true;
            }
        }
        return master;
    }

    @Transactional
    public void updateUserPassword(long userId, String password) {
        String newPasswordEncoding = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, newPasswordEncoding);
        LOG.debug("Password updated successfully for user id {} ", userId);
    }

    @Transactional
    public void blockUserbyOwnAdmin(long userId) {
        userRepository.blockUserbyOwnAdmin(userId);
        LOG.debug("the user id {}  has been blocked", userId);
    }

    @Transactional
    public void unBlockUserbyOwnAdmin(long userId) {
        userRepository.unBlockUserbyOwnAdmin(userId);
        LOG.debug("the user id {}  has been blocked", userId);
    }


    @Transactional
    public void blockUserbyAdmin(long userId) {
        userRepository.blockUserbyAdmin(userId);
        LOG.debug("the user id {}  has been blocked by the admin", userId);
    }

    @Transactional
    public void unblockUserbyAdmin(long userId) {
        userRepository.unBlockUserbyAdmin(userId);
        LOG.debug("the user id {}  has been blocked by the admin", userId);
    }

    @Transactional
    public void changeUserToAdminGroup(long userId) {
        userRepository.changeUserToAdminGroup(userId);
        LOG.debug("the user id {}  is the new admin of the group", userId);
    }

    @Transactional
    public void changeAdminGroupToUser(long userId) {
        userRepository.changeAdminGroupToUser(userId);
        LOG.debug("the admin user id {}  is the now a simple user", userId);
    }


    @Transactional
    public void updateUserProfile(long userId,
                                 String firstName,
                                 String lastName,
                                 String designation,
                                 String department,
                                 String country,
                                 String dateOfBirth,
                                 String phoneNumber,
                                 String skipeAccount,
                                 String facebookUrl,
                                 String linkedIn){
        userRepository.updateUserProfile(userId, firstName,lastName,designation,department,country,dateOfBirth,phoneNumber,skipeAccount, facebookUrl, linkedIn);
        LOG.debug("user profil updated successfully for user id {} ", userId);
    }

    @Transactional
    public void updateMyAccount(long userId, String language, Boolean strongAUthfinal, Boolean alertAreaFinal) {
        userRepository.updateMyAccount(userId,language, strongAUthfinal, alertAreaFinal);
        LOG.debug("user profil account updated successfully for user id {} ", userId);
    }

    @Transactional
    void updateUserActivationAccount(Boolean enabled, long userId) {
        userRepository.updateUserActivationAccount(enabled, userId);
        LOG.debug("Account updated successfully for user id {} ", userId);
    }

    @Transactional
    public void update2FA(long userId,Boolean active2fa,String token2FA, String urlToDisplayQRCode) {
        userRepository.update2FA(userId, active2fa, token2FA, urlToDisplayQRCode);
        LOG.debug("update2FA updated successfully for user id {} with active2fa at {}", userId, active2fa);
    }


    public  void updatestripeCustomerId(long userId, String stripeToken){
        userRepository.updateStripeCustomerId(stripeToken, userId);
        LOG.debug("user profil account updated successfully for user id {}  with Stripe Token ", userId);
    }


}
