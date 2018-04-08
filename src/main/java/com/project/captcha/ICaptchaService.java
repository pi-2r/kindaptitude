package com.project.captcha;

/**
 * Created by zen on 02/05/17.
 */
public interface ICaptchaService {
    boolean processResponse(final String response) throws ReCaptchaInvalidException;

    String getReCaptchaSite();

    String getReCaptchaSecret();
}