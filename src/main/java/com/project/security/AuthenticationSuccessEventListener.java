package com.project.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.GetUserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Created by zen on 17/06/17.
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private HttpSession session;
    @Autowired
    private GetUserInformation getUserInformation;



    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {

        loginAttemptService.loginSucceeded(request.getParameter("username"));

        //------- we load all information about user
        User user = (User) event.getAuthentication().getPrincipal();
        getUserInformation.setAllInformationAboutUser(session, user);

        //-------- check if double authentification is activeted
        if (Boolean.valueOf(user.getStrongAUth())) {
            session.setAttribute("valide2fa", false);
        }
        else{
            session.setAttribute("valide2fa", true);
        }
    }


}