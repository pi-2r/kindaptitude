package com.project.backend.service;

import com.project.captcha.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

/**
 * Created by zen on 02/05/17.
 */
@Service("CaptchaService")
public class CaptchaService implements ICaptchaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CaptchaSettings captchaSettings;

    @Autowired
    private ReCaptchaAttemptService reCaptchaAttemptService;

    @Autowired
    private RestOperations restTemplate;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public boolean processResponse(final String response) {

        boolean captchaResponse =false;
        LOGGER.debug("Attempting to validate response {}", response);

        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            LOGGER.warn("Client exceeded maximum number of failed attempts");
            //throw new ReCaptchaInvalidException("Client exceeded maximum number of failed attempts");
        }

        if (!responseSanityCheck(response)) {
            LOGGER.warn("Response contains invalid characters");
            //throw new ReCaptchaInvalidException("Response contains invalid characters");
        }

        final URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            LOGGER.debug("Google's response: {} ", googleResponse.toString());
            LOGGER.debug("googleResponse.isSuccess() ===>" + googleResponse.isSuccess());
            if (!googleResponse.isSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
            }
            captchaResponse = googleResponse.isSuccess();

        } catch (RestClientException rce) {
            LOGGER.warn("Registration unavailable at this time.  Please try again later.", rce);
            //throw new ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce);
        }
        finally {
            reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
            return captchaResponse;
        }

        //return captchaResponse;
    }

    private boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @Override
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
