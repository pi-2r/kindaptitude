package com.project.web.controllers;

import com.project.backend.service.GetUserInformation;
import com.project.security.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


/**
 * Created by zen on 09/07/17.
 */
@Controller
public class UserHistoryController {

    public static final String USER_HISTORY_NAVIGATION = "user/userhistory";
    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    protected Audit logUser;

    @RequestMapping("/yourhistory")
    public String UserHome(HttpSession session, Device device)
    {
        if(!Boolean.valueOf(session.getAttribute("valide2fa").toString())) {
            return "redirect:/check2fa.html?error=true";
        }


        //getAllInformationAboutUser(session);
        return USER_HISTORY_NAVIGATION;
    }
}
