package com.project.backend.service;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.captcha.ICaptchaService;
import com.project.config.I18NConfig;
import com.project.security.LoginAttemptService;
import com.project.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


/**
 * Created by tedonema on 31/03/2016.
 */
@Service
public class UserSecurityService implements UserDetailsService {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);
    public static final String EMAIL_MESSAGE_TEXT_FOR_BLOCKED_ACCOUNT = "blockedAccount.email.text1";
    public static final String EMAIL_MESSAGE_TEXT_FOR_BLOCKED_ACCOUNT2 = "blockedAccount.email.text2";
    public static final String EMAIL_MESSAGE_TEXT_FOR_BLOCKED_SUBJECT = "blockedAccount.email.subject";

    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private I18NService i18NService;
    @Autowired
    public SendEmail sendEmail;
    @Autowired
    GeoIP2 geoIP2;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private UserService userService;
    @Autowired
    private ICaptchaService captchaService;


    @Value("${webmaster.email}")
    private String webMasterEmail;

    @Value("${numberOfBadPassword}")
    private int maxFalsePassword;

    @Value("${enableCaptcha}")
    private Boolean enableCaptcha;


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        final String ip = getUserInformation.getClientIP();
        final String response = request.getParameter("g-recaptcha-response");

        if (enableCaptcha) {

            if (!captchaService.processResponse(response)) {
                LOG.error("The user: " + userEmail + " not valide the captcha");
                return null;
            }
        }
        // check if is a valid email
        if(!emailValidator.validate(userEmail))
        {
            throw new UsernameNotFoundException("The string is: " + userEmail + " is not a valid email");
        }
        User user = userRepository.findByEmail(userEmail);

        if(user.isBlockedByAdmin() || user.isBlockedByOwnAdmin() || !user.isEnabled())
        {
            return null;
        }

        // check if the user is not blocked
        if (loginAttemptService.isBlocked(userEmail)) {
            //-------- send email if the count is true
            if(loginAttemptService.getAttemptsCache(userEmail) >= maxFalsePassword) {

                userService.updateUserActivationAccount(false, user.getId());
                //-------- send email
                I18NConfig language = new I18NConfig();
                mailClient.blockAccount(userEmail, user.getFirstName(), ip, language.localeResolver().resolveLocale(request));
                loginAttemptService.loginSucceeded(userEmail);

                throw new UsernameNotFoundException("The string is: " + userEmail + " is blocked");
            }
        }

        if (null == user) {
            LOG.warn("UserEmail {} not found", userEmail);
            throw new UsernameNotFoundException("UserEmail " + userEmail + " not found");
        }
        LOG.warn("email found");

        //--------- check Ip Address
        if (user.getAlertArea())
        {
            if(checkIPAddress(ip, user.getAreaConnexion())){
                //---- notify at the user that your hacked has may be hacked
                mailClient.alerteArea(userEmail,request.getLocale(), user.getFirstName(), ip, "browser", getUserInformation.getBrowserInfo(), geoIP2.GeoIp2ByCity(ip));
            }
        }
        return user;
    }

    public void alerteByEmailUserBloked(String userEmail, String firstName, String ip) {

        LOG.debug("The user has been blocked");

        String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_FOR_BLOCKED_ACCOUNT, request.getLocale());
        String emailText2 = i18NService.getMessage(EMAIL_MESSAGE_TEXT_FOR_BLOCKED_ACCOUNT2, request.getLocale());
        String subject = i18NService.getMessage(EMAIL_MESSAGE_TEXT_FOR_BLOCKED_SUBJECT, request.getLocale());

        //--- call email sender
        //mailClient.prepareAndSend(userEmail, "demo html", "plain");
        //mailClient.blockAccount(userEmail, firstName, ip);
    }


    private Boolean checkIPAddress(String ip, String areaConnexion)
    {
        boolean badAreaConnexion = false;
        //-----test ip address
        if(!areaConnexion.equals(geoIP2.GeoIp2ByCity(ip))) {
            badAreaConnexion = true;
        }
        return badAreaConnexion;
    }
}
