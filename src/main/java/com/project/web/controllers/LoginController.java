package com.project.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * Created by zen on 09/03/17.
 */
@Controller
public class LoginController {

    /** The login view name */
    private  static String LOGIN_VIEW_NAME = "user/login";
    @Value("${enableCaptcha}")
    private Boolean enableCaptcha;


    @RequestMapping("login")
    public String loginPage(Locale locale)
    {

        return LOGIN_VIEW_NAME;
    }
}
