package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.*;
import com.project.security.Audit;
import com.project.security.PasswordValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by zen on 08/06/17.
 */
@Controller
public class ProfileController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);
    @Autowired
    private I18NService i18NService;
    @Autowired
    protected Audit logUser;
    @Autowired
    private UserService userService;
    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    private CompanyService companyService;
    @Autowired
    protected EmailValidator emailValidator;
    @Autowired
    protected PasswordValidator passwordValidator;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MailClient mailClient;

    public static final String USER_PROFILE3 = "user/profile3";

    @Value("${availableLanguage}")
    private String availableLanguage;

    /** The encryption salt */
    @Value("${saltPassword}")
    private String SALT;
    @Value("${strengthBCryptPasswordEncoder}")
    private int strengthBCryptPasswordEncoder;

    private static final String PROFILE_ATTRIBUTE_NAME = "profileMessage";
    private static final String MESSAGE_ATTRIBUTE_NAME = "message";
    private static final String CHANGE_PROFILE_VIEW_NAME ="user/profile";
    private static final String REDIRECT_PAGE ="profile";
    private static final String BIRTHDAY_PATTERN_FR ="\\d{2}/\\d{2}/\\d{4}";
    private static final String BIRTHDAY_PATTERN_US ="\\d{4}/\\d{2}/\\d{2}";
    private static final String VALIDE_URL = "(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?";

    UrlValidator urlValidator = new UrlValidator();


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(HttpSession session,
                              Device device,
                              Model model, Locale locale,
                              @RequestParam(value = "language", required = false) String language,
                              @RequestParam(value = "alerteArea", required = false) String alerteArea,
                              @RequestParam(value = "accountSettings", required = false) String accountSettings,
                              @RequestParam(value = "firstName", required = false) String firstName,
                              @RequestParam(value = "country", required = false) String country,
                              @RequestParam(value = "dateOfBirthDay", required = false) String dateOfBirthDay,
                              @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                              @RequestParam(value = "changeProfile", required = false) String changeProfile,
                              @RequestParam(value = "nameOfComapny", required = false) String nameOfComapny,
                              @RequestParam(value = "kindOfOrganisation", required = false) String kindOfOrganisation,
                              @RequestParam(value = "sirenOfCompany", required = false) String sirenOfCompany,
                              @RequestParam(value = "tva", required = false) String tva,
                              @RequestParam(value = "street", required = false) String street,
                              @RequestParam(value = "zipCode", required = false) String zipCode,
                              @RequestParam(value = "town", required = false) String town,
                              @RequestParam(value = "emailContact", required = false) String emailContact,
                              @RequestParam(value = "phoneNumberContact", required = false) String phoneNumberContact,
                              @RequestParam(value = "websiteCompany", required = false) String websiteCompany,
                              @RequestParam(value = "linkedinPageCompany", required = false) String linkedinPageCompany,
                              @RequestParam(value = "valideCompany", required = false) String valideCompany,
                              @RequestParam(value = "rulepassword", required = false) String rulepassword,
                              @RequestParam(value = "oldpasswordgood", required = false) String oldpasswordgood
                              )
    {
        if ("false".equals(language)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.languageBad", locale));
        }
         if ("false".equals(alerteArea)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.alerteAreaBad", locale));
        }
         if ("false".equals(firstName)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideFirstNameOrLastName", locale));
        }
         if ("false".equals(country)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideCountry", locale));
        }
         if ("false".equals(dateOfBirthDay)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideBirthDayDate", locale));
        }
         if ("false".equals(phoneNumber)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidephoneNumber", locale));
        }
         if ("false".equals(nameOfComapny)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidenameOfComapny", locale));
        }
         if ("false".equals(nameOfComapny)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidekindOfOrganisation", locale));
        }
         if ("false".equals(kindOfOrganisation)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidekindOfOrganisation", locale));
        }
         if ("false".equals(sirenOfCompany)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidesirenOfCompany", locale));
        }
         if ("false".equals(tva)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideTva", locale));
        }
         if ("false".equals(street)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidestreet", locale));
        }
         if ("false".equals(zipCode)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidezipCode", locale));
        }
         if ("false".equals(town)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidetown", locale));
        }
         if ("false".equals(emailContact)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideemailContact", locale));
        }
         if ("false".equals(phoneNumberContact)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalidephoneNumberContact", locale));
        }
         if ("false".equals(websiteCompany)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideUrlWebsite", locale));
        }
         if ("false".equals(linkedinPageCompany)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.invalideLinkedinUrl", locale));
        }
         if ("false".equals(rulepassword)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.rulepassword", locale));
        }
         if ("false".equals(oldpasswordgood)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.oldpasswordgood", locale));
        }



         if ("true".equals(changeProfile)){
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "true");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.valideChangeProfil", locale));
        }
         if ("true".equals(accountSettings)) {
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "true");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.updateSettingsAccount", locale));
        }

         if("true".equals(valideCompany)){
            model.addAttribute(PROFILE_ATTRIBUTE_NAME, "true");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("profile.valideCompany", locale));
        }

        return CHANGE_PROFILE_VIEW_NAME;
    }


    @RequestMapping(value = "/profile1", method = RequestMethod.POST)
    public String postChangeProfil(HttpSession session,
                                   HttpServletRequest request,
                                   @RequestParam(value = "firstName") String firstName,
                                   @RequestParam(value = "lastName") String lastName,
                                   @RequestParam("designation") String designation,
                                   @RequestParam("department") String department,
                                   @RequestParam(value = "countrySelectBoxProfil") String country,
                                   @RequestParam(value = "dateOfBirth") String dateOfBirth,
                                   @RequestParam(value = "phoneNumber") String phoneNumber,
                                   @RequestParam("skipeAccount") String skipeAccount,
                                   @RequestParam("facebookUrl") String facebookUrl,
                                   @RequestParam("linkedIn") String linkedIn,
                                   ModelMap model, Device device,
                                   Locale locale) {


            Long userId = Long.parseLong(session.getAttribute("userId").toString());
            if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)) {
                LOG.warn("Invalid user firstName  or user lastName for userId{} ", userId);
                return "redirect:profile?firstNameOrlastName=false";
            }

            if (StringUtils.isEmpty(country)) {
                LOG.warn("Invalid country");
                return "redirect:profile?country=false";
            }

            if (StringUtils.isEmpty(dateOfBirth)) {
                LOG.warn("Invalid dateOfBirth");
                return "redirect:profile?dateOfBirthDay=false";
            }
            if(!detectAvailableLanguage(country))
            {
                LOG.error("bad language, userid: " + userId);
                return "redirect:/profile?language=false";
            }


        if (StringUtils.isEmpty(phoneNumber)) {
            LOG.warn("Invalid phoneNumber");
            return "redirect:profile?phoneNumber=false";
        }
            //---- check the language of the user, and adapte the regex
            if ("fr".equals(locale.getLanguage())) {
                if (!dateOfBirth.matches(BIRTHDAY_PATTERN_FR)) {
                    return "profile?dateOfBirthDay=false";
                }
            } else {
                if (!dateOfBirth.matches(BIRTHDAY_PATTERN_US)) {
                    return "profile?dateOfBirthDay=false";
                }
            }

            userService.updateUserProfile(userId, firstName, lastName, designation, department, country, dateOfBirth, phoneNumber, skipeAccount, facebookUrl, linkedIn);

            //------ update session information
            User user = userService.findById(userId);
            getUserInformation.setAllInformationAboutUser(session, user);
            return "redirect:profile?changeProfile=true";
    }

    @RequestMapping(value = "/profile1", method = RequestMethod.GET)
    public String getPage1(HttpSession session, Device device) {
        return UserController.USER_PROFILE;
    }


    @RequestMapping(value ="/profile3", method = RequestMethod.POST)
    public String postChangeBusiness(HttpSession session,
                                     HttpServletRequest request,
                                     ModelMap model, Device device,
                                     @RequestParam(value = "nameOfComapny") String nameOfComapny,
                                     @RequestParam(value = "kindOfOrganisation") String kindOfOrganisation,
                                     @RequestParam(value = "sirenOfCompany") String sirenOfCompany,
                                     @RequestParam(value = "tva") String tva,
                                     @RequestParam(value = "street") String street,
                                     @RequestParam(value = "zipCode") String zipCode,
                                     @RequestParam(value = "town") String town,
                                     @RequestParam(value = "countryCompany") String country,
                                     @RequestParam(value = "emailContact") String emailContact,
                                     @RequestParam(value = "phoneNumberContact") String phoneNumberContact,
                                     @RequestParam(value = "websiteCompany") String websiteCompany,
                                     @RequestParam(value = "linkedinPageCompany") String linkedinPageCompany,
                                     Locale locale) {

        if (StringUtils.isEmpty(nameOfComapny)) {
            LOG.error("Invalid nameOfComapny");
            return "redirect:profile?nameOfComapny=false";
        }

        if (StringUtils.isEmpty(kindOfOrganisation)) {
            LOG.error("Invalid kindOfOrganisation");
            return "redirect:profile?kindOfOrganisation=false";
        }

        if (StringUtils.isEmpty(sirenOfCompany)) {
            LOG.error("Invalid sirenOfCompany");
            return "redirect:profile?sirenOfCompany=false";
        }

        if (StringUtils.isEmpty(tva)) {
            LOG.error("Invalid tva company");
            return "redirect:profile?tva=false";
        }

        if (StringUtils.isEmpty(street)) {
            LOG.error("Invalid street company");
            return "redirect:profile?street=false";
        }
        if (StringUtils.isEmpty(zipCode)) {
            LOG.error("Invalid zipCode");
            return "redirect:profile?zipCode=false";
        }
        if (StringUtils.isEmpty(town)) {
            LOG.error("Invalid town company");
            return "redirect:profile?town=false";
        }

        if (StringUtils.isEmpty(emailContact) || !emailValidator.validate(emailContact)) {
            LOG.error("Invalid emailContact");
            return "redirect:profile?emailContact=false";
        }

        if (StringUtils.isEmpty(country)) {
            LOG.error("Invalid country company");
            return "redirect:profile?country=false";
        }

        if (StringUtils.isEmpty(phoneNumberContact)) {
            LOG.error("Invalid phoneNumberContact");
            return "redirect:profile?phoneNumberContact=false";
        }


        if (!StringUtils.isEmpty(websiteCompany)) {
            if (!urlValidator.isValid(websiteCompany)){
                LOG.error("Invalid websiteCompany");
                return "redirect:profile?websiteCompany=false";
            }
        }

        if (!StringUtils.isEmpty(linkedinPageCompany)) {
            if (!urlValidator.isValid(linkedinPageCompany)){
                LOG.error("Invalid linkedinPageCompany");
                return "redirect:profile?linkedinPageCompany=false";
            }
        }



        Long companyId = Long.parseLong(session.getAttribute("companyId").toString());
        Long userId = Long.parseLong(session.getAttribute("userId").toString());
       companyService.updateCompanyProfile(companyId,
                nameOfComapny,
                kindOfOrganisation,
                street,
                zipCode,
                country,
                town,
                websiteCompany,
                sirenOfCompany,
                "",
                tva,
                "",
                "",
                "",
                linkedinPageCompany,
                emailContact,
                phoneNumberContact);


        //------ update session information
        User user = userService.findById(userId);
        getUserInformation.setAllInformationAboutUser(session, user);
        return "redirect:profile?valideCompany=true";

    }

    @RequestMapping(value = "/profile3", method = RequestMethod.GET)
    public String getPage3() {
        return UserController.USER_PROFILE;
    }

    @RequestMapping(value = "/yourbusiness", method = RequestMethod.GET)
    public String getyourbusiness() {
        return UserController.USER_PROFILE;
    }

    @RequestMapping(value = "/profile2", method = RequestMethod.GET)
    public String getPage2() {
        return UserController.USER_PROFILE;
    }

    @RequestMapping(value ="/profile2", method = RequestMethod.POST)
    public String postChangeAccount(HttpSession session,
                                     HttpServletRequest request,
                                     ModelMap model, Device device,
                                     @RequestParam(value = "language") String language,
                                     @RequestParam(value = "alertArea") Boolean alertArea,
                                     @RequestParam(value = "2fa") Boolean active2fa,
                                     @RequestParam(value = "oldpassword") String oldpassword,
                                     @RequestParam(value = "newPassword") String newPassword,
                                     @RequestParam(value = "confirmNewPassword") String confirmNewPassword,
                                     Locale locale) {
        Boolean strongAUthfinal =  null;
        Long companyId = Long.parseLong(session.getAttribute("companyId").toString());
        Long userId = Long.parseLong(session.getAttribute("userId").toString());


        if(!detectAvailableLanguage(language))
        {
            LOG.error("bad language, userid: " + userId);
            return "redirect:/profile?language=false";
        }
        if ("true".equals(alertArea) || "false".equals(alertArea)) {
            LOG.error("bad alertArea, userid: " + userId);
            return "redirect:/profile?alerteArea=false";
        }
        if ("true".equals(active2fa) || "false".equals(active2fa)) {
            LOG.error("bad alertArea, userid: " + userId);
            return "redirect:/profile?alerteArea=false";
        }

        userService.updateMyAccount(userId,language,active2fa,alertArea);

        if(!StringUtils.isEmpty(oldpassword)) {
            if (testOldPassword(userId, oldpassword)) {

                if (!newPassword.isEmpty() && !confirmNewPassword.isEmpty()) {

                    if (passwordValidator.validate(newPassword)) {
                        if (newPassword.equals(confirmNewPassword)) {
                            //------ update password
                            userService.updateUserPassword(userId, confirmNewPassword);
                            //----- send information
                            //--- send sample email
                            mailClient.changePassword(session.getAttribute("email").toString(),
                                    locale,
                                    session.getAttribute("firstName").toString(),
                                    session.getAttribute("lastName").toString());
                        }
                    } else {
                        LOG.info("userid: " + userId + ", bad rule password");
                        return "redirect:/profile?rulepassword=false";
                    }

                } else {
                    LOG.error("userid: " + userId + ", bad new or confirme new password");
                    return "redirect:/profile?badpassword=false";
                }

            } else {
                LOG.error("userid: " + userId + ", bad old password");
                return "redirect:/profile?oldpasswordgood=false";
            }
        }


        //------ update session information
        User user = userService.findById(userId);
        getUserInformation.setAllInformationAboutUser(session, user);
        return "redirect:profile?accountSettings=true";
    }


    private Boolean detectAvailableLanguage(String language){
        Boolean flag =  false;
        String[] result = availableLanguage.split(";");
        for( int i = 0; i < result.length; i++)
        {
            if(result[i].equals(language.toLowerCase()))
            {
                return  flag = true;
            }

        }
        return flag;
    }

    private Boolean testOldPassword(Long userId, String oldpassword){
        Boolean flag = false;
        User getOldUserPassword = userService.findById(userId);
        // ----- test old password with the database information
        if(passwordEncoder.matches(oldpassword, getOldUserPassword.getPassword())) {
            flag= true;
        }
        return flag;
    }
}
