package com.project.web.controllers;

import com.project.captcha.ICaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zen on 02/05/17.
 */
@Controller
public class RegistrationCaptchaController {

    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationCaptchaController.class);

    /** The login view name */
    private  static String REGISTRATION_VIEW_NAME = "user/registrationCaptcha";
    private static final String BAD_REGISTRATION = "redirect:/registrationCaptcha?error=true";

    @Autowired
    private ICaptchaService captchaService;


    // Registration
    @RequestMapping(value = "registrationCaptchaValidation", method = RequestMethod.POST)
    public String captchaRegisterUserAccount(final HttpServletRequest request) {

        final String response = request.getParameter("g-recaptcha-response");
        if (!response.isEmpty()){
            if (captchaService.processResponse(response)) {
                LOGGER.debug("Registering user account with information: {}");
                return "yolo";
            }
        }

        LOGGER.debug("No Registering user account...");
        return BAD_REGISTRATION;
    }


    @RequestMapping("registrationCaptcha")
    public String displayCaptchaRegisterUserAccount() {
        return REGISTRATION_VIEW_NAME;
    }

}
