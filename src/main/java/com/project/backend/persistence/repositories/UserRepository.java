package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zen on 14/05/17.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns a User given a username or null if not found.
     * @param username The username
     * @return a User given a username or null if not found.
     */
    User findByUsername(String username);

    /**
     * Returns a User given a email or null if not found.
     * @param email
     * @return
     */
    User findByEmail(String email);

    User findById(long id);

    @Query("SELECT DISTINCT u FROM User u, Company c LEFT JOIN u.company c where c.id = :company and u.company = c.id and u.masterGroupe= true")
    User findMasterGroupByCompanyId(@Param("company") Long  companyId);

    @Query("SELECT DISTINCT u FROM User u, Company c LEFT JOIN u.company c where c.id = :company and u.company = c.id")
    List<User> findByCompany(@Param("company") long company);

    @Query("SELECT DISTINCT u FROM User u, Company c LEFT JOIN u.company c where  u.activUserFromOwnerGroup = true and c.id = :company and u.company = c.id")
    List<User> getAllNewUserByCompany(@Param("company") long company);

    @Modifying
    @Query("update User u set u.password = :password, u.enabled = true where u.id = :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);

    @Modifying
    @Query("update User u set  u.blockedByOwnAdmin = true where u.id = :userId")
    void blockUserbyOwnAdmin(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.blockedByAdmin = true where u.id = :userId")
    void blockUserbyAdmin(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.blockedByOwnAdmin = false, u.activUserFromOwnerGroup = false where u.id = :userId")
    void unBlockUserbyOwnAdmin(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.blockedByOwnAdmin = false, u.activUserFromOwnerGroup = false where u.id = :userId")
    void unBlockUserbyAdmin(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.masterGroupe = 1 where u.id = :userId")
    void changeUserToAdminGroup(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.masterGroupe = 0 where u.id = :userId")
    void changeAdminGroupToUser(@Param("userId") long userId);

    @Modifying
    @Query("update User u set  u.strongAUth = :active2fa, u.token2FA = :token2FA, " +
            "u.urlToDisplayQRCode = :urlToDisplayQRCode where u.id = :userId")
    void update2FA(@Param("userId") long userId,
                   @Param("active2fa") Boolean active2fa,
                   @Param("token2FA") String token2FA,
                   @Param("urlToDisplayQRCode") String urlToDisplayQRCode);

    @Modifying
    @Query("update User u set u.firstName = :firstName, u.lastName = :lastName, u.designation = :designation, " +
            "u.department = :department, u.country = :country, u.birthdayDate = :dateOfBirth, u.phoneNumber = :phoneNumber," +
            "u.skypeNumer = :skipeAccount, u.linkedinAccount = :linkedIn, u.facebookAccount = :facebookUrl where u.id = :userId")
    void updateUserProfile(@Param("userId") long userId,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("designation") String designation,
                          @Param("department") String department,
                          @Param("country") String country,
                          @Param("dateOfBirth") String dateOfBirth,
                          @Param("phoneNumber") String phoneNumber,
                          @Param("skipeAccount") String skipeAccount,
                          @Param("facebookUrl") String facebookUrl,
                          @Param("linkedIn") String linkedIn);

    @Query("select u from User u where u.email like %?1%")
    User findByEmailAddress(String emailAddress);


    //---- find all user
    @Query("SELECT DISTINCT u FROM User u, Company c LEFT JOIN u.company c where u.company = c.id")
    List<User> findAllUser();

    @Modifying
    @Query("update User u set u.language = :language, u.strongAUth = :strongAUthfinal, u.alertArea = :alertAreaFinal  where u.id = :userId")
    void updateMyAccount(@Param("userId") long userId,
                         @Param("language") String language,
                         @Param("strongAUthfinal") Boolean strongAUthfinal,
                         @Param("alertAreaFinal") Boolean alertAreaFinal);

    @Modifying
    @Query("update User u set u.enabled = :enabled where u.id = :userId")
    void updateUserActivationAccount(@Param("enabled") Boolean enabled, @Param("userId") long userId);

    //@Modifying
    //@Transactional
    //@Query(value="DELETE FROM User u WHERE u.id = :userId")
    //@Query("delete from User u where u.id = :userId")
    void deleteById(long userId);

    @Modifying
    @Query("update User u set u.stripeCustomerId = :stripeCustomerId where u.id = :userId")
    void updateStripeCustomerId(@Param("stripeCustomerId") String stripeCustomerId, @Param("userId") long userId);

}
