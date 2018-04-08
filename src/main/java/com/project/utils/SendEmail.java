package com.project.utils;

import com.project.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by zen on 17/07/17.
 */
@Service
public class SendEmail {

    @Value("${default.to.address}")
    private String WEB_MASTER_EMAIL;

    @Autowired
    private EmailService emailService;

    public boolean sendEmailBuy(String email, String subject, String text, String kindOfEmail){
        String textValue = loadKindOfEmail(kindOfEmail, text);
        sendEmail(email, subject, textValue);
        return true;
    }

    public boolean sendEmail(String email, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailMessage.setFrom(WEB_MASTER_EMAIL);

        emailService.sendGenericEmailMessage(mailMessage);
        return true;
    }

    private String loadKindOfEmail(String kindOfEmail, String text) {
        return text;
    }

}
