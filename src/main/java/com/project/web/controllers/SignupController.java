package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.*;
import com.project.backend.service.*;
import com.project.captcha.ICaptchaService;
import com.project.enums.PlansEnum;
import com.project.enums.RolesEnum;
import com.project.security.PasswordValidator;
import com.project.security.ThrowawayDomains;
import com.project.security.WorstPassword;
import com.project.utils.RegexUtils;
import com.project.utils.UserUtils;
import com.project.web.domain.frontend.BasicAccountPayload;
import com.project.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by zen on 01/05/17.
 */
@Controller
public class SignupController {

    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private I18NService i18NService;
    @Autowired
    protected EmailValidator emailValidator;
    @Autowired
    protected PasswordValidator passwordValidator;
    @Autowired
    protected WorstPassword worstPassword;
    @Autowired
    protected ThrowawayDomains throwawayDomains;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private RegexUtils regexUtils;

    @Value("${enableCaptcha}")
    private Boolean enableCaptcha;

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    public static final String SIGNUP_URL_MAPPING = "/signup";
    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";
    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";
    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";
    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";
    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";
    public static  final String MESSAGE="message";



    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signup(ModelMap model) {

        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
        model.addAttribute("enableCaptcha", enableCaptcha);
        return SUBSCRIPTION_VIEW_NAME;
    }

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signUpPost(@ModelAttribute(PAYLOAD_MODEL_KEY_NAME) ProAccountPayload payload,
                             final HttpServletRequest request,
                             ModelMap model,
                             Locale locale) throws IOException {

        final String response = request.getParameter("g-recaptcha-response");
        if (response == null && enableCaptcha) {
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.error", locale));
            LOG.error("no captcha");
        }

            if (!captchaService.processResponse(response) && enableCaptcha) {

                model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.error", locale));
                LOG.error("no captcha");
            }

                if(throwawayDomains.isThrowawayDomains(payload.getEmail())) {
                    model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.badEmailDomain", locale));
                    return SUBSCRIPTION_VIEW_NAME;
                }

                if (emailValidator.validate(payload.getEmail())) {
                    LOG.debug("Registering user account with information: {}");
                    this.checkForDuplicates(payload, model);

                    boolean duplicates = false;

                    List<String> errorMessages = new ArrayList<>();


                if (model.containsKey(DUPLICATED_EMAIL_KEY)) {
                    LOG.warn("The email already exists. Displaying error to the user");
                    model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                    errorMessages.add("Email already exist");
                    duplicates = true;
                }

                if (duplicates) {
                    model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.dupicateEmail", locale));
                    return SUBSCRIPTION_VIEW_NAME;
                }

                if (!passwordValidator.validate(payload.getPassword()) && !worstPassword.isWorstPassword(payload.getPassword())) {
                    model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("resetPassword.token.invalidePassword", locale));
                    return SUBSCRIPTION_VIEW_NAME;
                }


                // There are certain info that the user doesn't set, such as profile image URL, Stripe customer id,
                // plans and roles
                LOG.debug("Transforming user payload into User domain object");
                User user = UserUtils.fromWebUserToDomainUser(payload);

                // Sets the Plan and the Roles (depending on the chosen plan)
                LOG.debug("Retrieving plan from the database");

                user.setPlan(planService.basicPlan());


                // By default users get the BASIC ROLE
                    Set<UserRole> roles = new HashSet<>();
                    roles.add(new UserRole(user, new Role(RolesEnum.USER)));

                //-- get domaine from email
                String domaineName =  regexUtils.getDomaineName("(?<=@)[^.]+",payload.getEmail());

                Company company = null;
                company = companyService.findByCompanyName(domaineName);
                if(company ==null) {
                    company =  new Company();
                    company.setCompanyName(domaineName);
                    //-- get website from email
                    String[] parts = payload.getEmail().split("@");
                    String createWebsite = "www." + parts[1];
                    company.setWebSite(createWebsite);
                    user.setMasterGroupe(true);
                    user.setEnabled(true);
                    user.setAlertArea(false);
                    user.setStrongAUth(false);
                    user.setEmail(payload.getEmail());
                    user.setUsername(payload.getFirstName());
                    user.setFirstName(payload.getFirstName());
                    user.setLastName(payload.getLastName());
                    user.setLanguage(locale.getLanguage());
                    userService.createUser(user, PlansEnum.USER, roles, company);
                    //--- send email at the new user
                    mailClient.signUpNewUserWithNoCompany(payload.getEmail(), payload.getFirstName(),
                            payload.getLastName(),payload.getPassword(), domaineName, locale);

                }else{
                    Long companyId =  company.getId();
                    User owner = userService.findMasterGroupByCompanyId(companyId);
                    user.setBlockedByOwnAdmin(true);
                    user.setEnabled(true);
                    user.setMasterGroupe(false);
                    user.setActivUserFromOwnerGroup(true);
                    user.setAlertArea(false);
                    user.setStrongAUth(false);

                    user.setEmail(payload.getEmail());
                    user.setUsername(payload.getFirstName());
                    user.setFirstName(payload.getFirstName());
                    user.setLastName(payload.getLastName());
                    user.setCompany(owner.getCompany());
                    user.setBlockedByAdmin(false);
                    user.setPassword(payload.getPassword());
                    user.setAlertArea(false);
                    user.setLanguage(owner.getLanguage());
                    user.setCountry(owner.getCountry());

                    user.setPlan(owner.getPlan());

                    //-- get website from email
                    userService.createUser(user, PlansEnum.USER, roles, owner.getCompany());
                    //--- send email at the new user
                    mailClient.signUpNewUserWithCompany(payload.getEmail(), payload.getFirstName(), payload.getLastName(),payload.getPassword(), locale);
                    //---- send infomation at thte owner of the group
                }





                // Auto logins the registered user
                /*Authentication auth = new UsernamePasswordAuthenticationToken(
                        registeredUser, null, registeredUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);*/

                LOG.info("User created successfully");


                model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.successful", locale));
                LOG.error("signup successful");
                }else
                {
                    model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.badEmail", locale));
                    LOG.error("bad email: ", payload.getEmail());
                }





        return SUBSCRIPTION_VIEW_NAME;
    }


    //--------------> Private methods

    /**
     * Checks if the username/email are duplicates and sets error flags in the model.
     * Side effect: the method might set attributes on Model
     **/
    private void checkForDuplicates(BasicAccountPayload payload, ModelMap model) {
        LOG.error("payload.getEmail() ===> " + payload.getEmail());
        if (userService.findByEmail(payload.getEmail()) != null) {
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }

    }
}
