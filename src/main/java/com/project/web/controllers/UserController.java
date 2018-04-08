package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.backend.service.GetUserInformation;
import com.project.security.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.project.security.TimeBasedOneTimePasswordUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;



/**
 * Created by zen on 10/03/17.
 */
@Controller
public class UserController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public static final String USER_VIEW = "user/home";
    public static final String USER_PROFILE = "user/profile";
    public static final String VALIDE_2FA ="user/valide2fa";
    private static final String LOGOUT_TO_LOGIN = "redirect:/login?logout";
    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    protected Audit logUser;
    @Autowired
    protected UserRepository user;


    @RequestMapping("/home")
    public String UserHome(HttpSession session, HttpServletRequest httpServletRequest, Device device)
    {
        if(!Boolean.valueOf(session.getAttribute("valide2fa").toString())) {
                        return "redirect:/check2fa.html?error=true";
        }
        return USER_VIEW;
    }

    @RequestMapping("/check2fa")
    public String Check2fa(HttpSession session, Device device)
    {
        String choisePath="";
        if(Boolean.valueOf(session.getAttribute("strongAUth").toString())) {
            choisePath = VALIDE_2FA;
        }
        else{
            return "redirect:/home.html";
        }
        return choisePath;
    }


    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response,
                              HttpSession session, Device device) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return LOGOUT_TO_LOGIN;
    }

    @RequestMapping("/validecheck2fa")
    public String valideCheck2fa(HttpSession session,
                                 Device device,
                                 Model model, Locale locale,
                                 @RequestParam(value="check2fa", required=false) String check2fa)
    {
        String choisePath = VALIDE_2FA;
        if(Boolean.valueOf(session.getAttribute("strongAUth").toString())) {
            if (check2fa != null || !check2fa.isEmpty()) {
                User ownUser = user.findByEmail(session.getAttribute("email").toString());
                String code = null;
                try {
                    code = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(ownUser.getToken2FA());
                } catch (Exception ex) {
                    LOG.error(ex.toString());
                    return "redirect:/check2fa.html?error=true";
                }

                if (check2fa.equals(code)) {
                    session.setAttribute("valide2fa", true);
                    return "redirect:/home.html";
                }else{
                    return "redirect:/check2fa.html?error=true";
                }
            }
            choisePath = VALIDE_2FA;
        }else
        {
            choisePath = USER_VIEW;
        }
        return choisePath;
    }
}
