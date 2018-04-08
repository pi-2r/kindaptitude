package com.project.security;

import com.project.captcha.ICaptchaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by zen on 30/08/17.
 */

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RecaptchaAuthenticationFilter.class);


    @Autowired
    private ICaptchaService captchaService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {


        final String captchaResponse = request.getParameter("g-recaptcha-response");
        if (!StringUtils.isEmpty(captchaResponse))
        {
            LOG.debug("ReCaptcha Challenge not null");
            if (!StringUtils.isEmpty(captchaResponse))
            {
                captchaService.processResponse(captchaResponse);
                if(captchaService.processResponse(captchaResponse)) {
                    LOG.debug("ReCaptcha answer is valid, attempt authentication");
                    return super.attemptAuthentication(request, response);
                }
                else {
                   // this.reCaptchaError(request, response, "ReCaptcha failed" );
                    return super.attemptAuthentication(request, response);
                }

            }
            else
            {
                this.reCaptchaError(request, response, "ReCaptcha failed : empty answer");
                return null;
            }

        }
        else
        {
            return super.attemptAuthentication(request, response);
        }
    }

    private void reCaptchaError(HttpServletRequest request, HttpServletResponse response, String errorMsg)
    {
        LOG.error("ReCaptcha failed : " + errorMsg);
        try
        {

            RequestDispatcher dispatcher = request.getRequestDispatcher("/login?error=2");

            dispatcher.forward(request, response);
        }
        catch (ServletException e)
        {
            throw new AuthenticationServiceException("ReCaptcha failed : " + errorMsg);
        }
        catch (IOException e)
        {
            throw new AuthenticationServiceException("Recaptcha failed : " + errorMsg);
        }
    }
}
