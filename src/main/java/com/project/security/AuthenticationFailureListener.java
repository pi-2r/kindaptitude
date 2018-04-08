package com.project.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * Created by zen on 17/06/17.
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;


    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
        loginAttemptService.loginFailed(request.getParameter("username"));
    }

}