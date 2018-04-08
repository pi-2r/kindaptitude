package com.project.config;

import com.project.backend.service.EmailService;
import com.project.backend.service.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by zen on 08/03/17.
 */
@Configuration
@Profile("prod")
@PropertySource("file://${user.home}/.devKindaptitude/application-prod.properties")
public class ProductionConfig {
    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}

