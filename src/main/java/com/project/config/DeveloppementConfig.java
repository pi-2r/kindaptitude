package com.project.config;

import com.project.backend.service.EmailService;
import com.project.backend.service.SmtpEmailService;
//import org.h2.server.web.WebServlet;
//import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by zen on 08/03/17.
 */
@Configuration
@Profile("dev")
@PropertySource("file://${user.home}/.devKindaptitude/application-dev.properties")
public class DeveloppementConfig {

    /*@Bean
    public EmailService emailService(){
        return new MockEmailService();
    }*/
    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }

    /*@Bean
    public ServletRegistrationBean h2ConsoleServletRegistration() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }*/

}
