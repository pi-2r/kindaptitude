package com.project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

/**
 * Created by zen on 22/08/17.
 */
@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String message, String kindOfEmail, Locale locale) {
        Context context = new Context(locale);
        context.setVariable("message", message);
        return templateEngine.process("email/"+kindOfEmail, context);
    }

    public String buildWelcome(String kindOfEmail, Context context) {
        return templateEngine.process("email/"+kindOfEmail, context);
    }


    public String buildchangePassword(String kindOfEmail, Context context) {
        return templateEngine.process("email/"+kindOfEmail, context);
    }
}
