package com.project.backend.service;

import com.project.backend.persistence.domain.backend.User;
import com.project.config.I18NConfig;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.mobile.device.Device;

import java.util.Locale;


/**
 * Created by zen on 27/05/17.
 */
@Service
public class GetUserInformation {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private I18NConfig i18NConfig;

    private String clientEmail;
    private String userId;
    private String sessionId;

    public void setAllInformationAboutUser(HttpSession session, User user) {
        session.setAttribute("firstName", user.getFirstName());
        session.setAttribute("lastName", user.getLastName());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("userId", user.getId());
        setUserId(String.valueOf(user.getId()));
        setClientEmail(user.getEmail());
        setSessionId(session.getId());
        session.setAttribute("optionChoise", user.getOptionChoise());
        session.setAttribute("designation", user.getDesignation());
        session.setAttribute("departement", user.getDepartment());
        session.setAttribute("country", user.getCountry());
        session.setAttribute("language", user.getLanguage());
        session.setAttribute("leftpanelCollapsed", user.getLeftpanelCollapsed());
        //------ change User language
        changeLanguageUserSpace(user);

        session.setAttribute("birthdayDate", user.getBirthdayDate());
        session.setAttribute("skypeNumer", user.getSkypeNumer());
        session.setAttribute("facebookAccount", user.getFacebookAccount());
        session.setAttribute("linkedinAccount", user.getLinkedinAccount());
        session.setAttribute("credits", user.getCredits());
        session.setAttribute("phone", user.getPhoneNumber());
        session.setAttribute("plan", user.getPlan());
        session.setAttribute("companyName", user.getCompany().getCompanyName());
        session.setAttribute("masterGroupe", user.getMasterGroupe());
        session.setAttribute("strongAUth", user.getStrongAUth());
        session.setAttribute("alertArea", user.getAlertArea());
        session.setAttribute("strongAUth", user.getStrongAUth());
        session.setAttribute("urlToDisplayQRCode", user.getUrlToDisplayQRCode());
        session.setAttribute("companyId", user.getCompany().getId());
        if(user.getMasterGroupe())
        {
            session.setAttribute("stripeToken", user.getStripeCustomerId());
            session.setAttribute("streetCompany", user.getCompany().getStreet());
            session.setAttribute("zipCodeCompany", user.getCompany().getZipCode());
            session.setAttribute("countryCompany", user.getCompany().getCountry());
            session.setAttribute("townCompany", user.getCompany().getTown());
            session.setAttribute("webSiteCompany", user.getCompany().getWebSite());

            session.setAttribute("sirenCompany", user.getCompany().getSiren());
            session.setAttribute("vatNumberCompany", user.getCompany().getVatNumber());
            session.setAttribute("tvaNumberCompany", user.getCompany().getTvaNumber());
            session.setAttribute("phoneNumberCompany", user.getCompany().getPhoneNumberCompany());
            session.setAttribute("emailCompany", user.getCompany().getEmailCompany());

            session.setAttribute("aboutYourBusinessCompany", user.getCompany().getAboutYourBusiness());
            session.setAttribute("businessType", user.getCompany().getBusinessType());
            session.setAttribute("linkedinUrlCompany", user.getCompany().getLinkedinUrl());
            session.setAttribute("contactPersonEmail", user.getCompany().getContactPersonEmail());
            session.setAttribute("phoneNumberContact", user.getCompany().getPhoneNumberContact());
        }

        //---- check if the user is admin
        Boolean idAdmin = request.isUserInRole("ADMIN");
        session.setAttribute("isAdmin", request.isUserInRole("ADMIN"));
    }

    private void changeLanguageUserSpace(User user) {
        i18NConfig.getResolver().setDefaultLocale(new Locale(user.getLanguage()));
    }
    public static void getAllInformationAboutUser(HttpSession session) {

        session.getAttribute("firstName");
        session.getAttribute("lastName");
        session.getAttribute("email");
        session.getAttribute("optionChoise");
        session.getAttribute("credits");
        session.getAttribute("phone");
        session.getAttribute("plan");

        if(Boolean.valueOf(session.getAttribute("masterGroupe").toString()))
        {
            session.getAttribute("companyId");
            session.getAttribute("streetCompany");
            session.getAttribute("zipCodeCompany");
            session.getAttribute("countryCompany");
            session.getAttribute("townCompany");
            session.getAttribute("webSiteCompany");

            session.getAttribute("sirenCompany");
            session.getAttribute("vatNumberCompany");
            session.getAttribute("tvaNumberCompany");
            session.getAttribute("phoneNumberCompany");
            session.getAttribute("emailCompany");

            session.getAttribute("aboutYourBusinessCompany");
            session.getAttribute("businessType");
            session.getAttribute("linkedinUrlCompany");
        }

    }


    public final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public final String getBrowserInfo() {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getBrowser().getName() + "-" + userAgent.getBrowserVersion();
    }

    public final String getDevice(Device device) {
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        return deviceType;
    }

    public String getClientEmail() {return clientEmail;}
    public void setClientEmail(String clientEmail) {this.clientEmail = clientEmail;}
    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}
    public String getSessionId() { return sessionId;}
    public void setSessionId(String sessionId) {this.sessionId = sessionId; }
}
