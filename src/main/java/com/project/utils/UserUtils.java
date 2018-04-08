package com.project.utils;

import com.project.backend.persistence.domain.backend.User;
import com.project.web.controllers.ForgotMyPasswordController;
import com.project.web.domain.frontend.BasicAccountPayload;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zen on 26/04/17.
 */
public class UserUtils {
    /**
     * Non instantiable.
     */
    private UserUtils() {
        throw new AssertionError("Non instantiable");
    }

    /**
     * Creates a user with basic attributes set.
     * @return A User entity
     */
    public static User createBasicUser(String email) {

        User user = new User();
        user.setUsername("pierre");
        user.setPassword("pierre");
        user.setEmail("demo@gmail.com");
        user.setFirstName("Pierre");
        user.setLastName("Therrode");
        user.setPhoneNumber("");
        user.setCountry("FR");
        user.setLanguage("fr");
        user.setEnabled(true);
        user.setAlertArea(false);
        user.setStrongAUth(false);
        user.setMasterGroupe(true);
        user.setDescription("A basic user");
        user.setBirthdayDate("31/01/1989");
        user.setLeftpanelCollapsed(true);
        user.setOptionChoise("1#2");
        user.setProfileImageUrl("https://blabla.images.com/basicuser");
        user.setAreaConnexion("{\"cityName\":\"Paris\",\"countryName\":\"France\",\"postal\":\"75017\",\"state\":\"Île-de-France\"}");

        return user;
    }

    /**
     *
     * @param request
     * @param userId
     * @param token
     * @return
     */
    public static String createPasswordResetUrl(HttpServletRequest request, long userId, String token) {
        String passwordResetUrl =
                request.getScheme() + "://" +
                        request.getServerName() +
                        ":" +
                        request.getServerPort() +
                        request.getContextPath() +
                        ForgotMyPasswordController.CHANGE_PASSWORD_PATH +
                        "?id=" +
                        userId +
                        "&token=" +
                        token;
        return passwordResetUrl;

    }

    public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload) {
        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhoneNumber(frontendPayload.getPhoneNumber());
        user.setCountry(frontendPayload.getCountry());
        user.setEnabled(true);
        user.setDescription(frontendPayload.getDescription());

        return user;
    }
}
