package com.project.config;


import com.project.security.AuditRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zen on 09/06/17.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {


    @Autowired
    AuditRequestInterceptor auditRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditRequestInterceptor);
    }

}