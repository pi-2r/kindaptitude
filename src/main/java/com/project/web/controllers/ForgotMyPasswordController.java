package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.PasswordResetToken;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.*;
import com.project.config.I18NConfig;
import com.project.security.PasswordValidator;
import com.project.security.WorstPassword;
import com.project.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Created by zen on 05/05/17.
 */
@Controller
public class ForgotMyPasswordController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";
    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";
    public static final String MAIL_SENT_KEY = "mailSent";
    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";
    public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";
    private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";
    private static final String MESSAGE_ATTRIBUTE_NAME = "message";

    @Autowired
    private I18NService i18NService;
    @Autowired
    private EmailService emailService;
    @Value("${webmaster.email}")
    private String webMasterEmail;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    protected EmailValidator emailValidator;
    @Autowired
    protected PasswordValidator passwordValidator;
    @Autowired
    protected WorstPassword worstPassword;
    @Autowired
    private MailClient mailClient;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPasswordGet() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request,
                                     @RequestParam("email") String email,
                                     ModelMap model) {
        if(emailValidator.validate(email)) {
            PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

            if (null == passwordResetToken) {
                LOG.warn("Couldn't find a password reset token for email {}", email);
                model.addAttribute(MAIL_SENT_KEY, "false");
            } else {

                User user = passwordResetToken.getUser();
                String token = passwordResetToken.getToken();

                String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
                LOG.debug("Reset Password URL {}", resetPasswordUrl);

                String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
                String subject = "[Devopsbuddy]: How to Reset Your Password";
                //-------- send email
                I18NConfig language = new I18NConfig();
                mailClient.forgotPassword(user.getEmail(), user.getFirstName(), resetPasswordUrl, language.localeResolver().resolveLocale(request));
                model.addAttribute(MAIL_SENT_KEY, "true");
            }
        }
        else {
            LOG.warn("Is not a Email {}: ", email);
            model.addAttribute(MAIL_SENT_KEY, "badEmail");
        }
        return EMAIL_ADDRESS_VIEW_NAME;
    }


    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changeUserPasswordGet(@RequestParam(value="id",  required=false) String id,
                                        @RequestParam(value="token",  required=false) String token,
                                        Locale locale,
                                        ModelMap model) {
        try {
            if (StringUtils.isEmpty(token) || Long.valueOf(id) == 0 || Long.valueOf(id) < 0) {
                LOG.error("Invalid user id {}  or token value {}", id, token);
                model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
                model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalideIdOrToken", locale));
                return CHANGE_PASSWORD_VIEW_NAME;
            }

            PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

            if (null == passwordResetToken) {
                LOG.warn("A token couldn't be found with value {}", token);
                model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
                model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.notfound", locale));

                return CHANGE_PASSWORD_VIEW_NAME;
            }

            User user = passwordResetToken.getUser();
            if (user.getId() != Long.valueOf(id)) {
                LOG.error("The user id {} passed as parameter does not match the user id {} associated with the token {}",
                        id, user.getId(), token);
                model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
                model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
                return CHANGE_PASSWORD_VIEW_NAME;
            }

            if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
                LOG.error("The token {} has expired", token);
                model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
                model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
                return CHANGE_PASSWORD_VIEW_NAME;
            }

            model.addAttribute("principalId", user.getId());

            // OK to proceed. We auto-authenticate the user so that in the POST request we can check if the user
            // is authenticated
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (Exception ex) {
            LOG.error("Invalid url, stacktrace: {}", ex);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalideIdOrToken", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPasswordPost(@RequestParam("principal_id") long userId,
                                         @RequestParam("password") String password,
                                         Locale locale,
                                         ModelMap model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            LOG.error("An unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) authentication.getPrincipal();
        if (user.getId() != userId) {
            LOG.error("Security breach! User {} is trying to make a password reset request on behalf of {}",
                    user.getId(), userId);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request.");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if (!passwordValidator.validate(password) && !worstPassword.isWorstPassword(password)) {
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalidePassword", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(userId, password);
        LOG.info("Password successfully updated for user {}", user.getUsername());
        //------- send email
        mailClient.newPassword(user.getEmail(), user.getFirstName(), locale);
        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

        return CHANGE_PASSWORD_VIEW_NAME;

    }


}
