package com.project.backend.service;

import com.project.web.domain.frontend.FeedbackPojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by zen on 14/05/17.
 */
public abstract class AbstractEmailService implements EmailService {

    @Value("${default.to.address}")
    private String defaultToAddress;

    /**
     * Creates a Simple Mail Message from a Feedback Pojo.
     * @param feedback The Feedback pojo
     * @return
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(FeedbackPojo feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[DevOps Buddy]: Feedback received from " + feedback.getFirstName() + " " + feedback
                .getLastName() + "! ");
        message.setTo(defaultToAddress);
        message.setFrom(feedback.getEmail());
        message.setText(feedback.getFeedback());
        return message;
    }


    protected SimpleMailMessage alertChangePassword(FeedbackPojo feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        return message;
    }
    
    protected  SimpleMailMessage alerteDuplicateSession(FeedbackPojo feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        return message;
    }

    @Override
    public void sendFeedbackEmail(FeedbackPojo feedbackPojo) {
        sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackPojo(feedbackPojo));
    }


}
