package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Created by tedonema on 07/03/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.project.web.controllers")
public class I18NConfig extends WebMvcConfigurerAdapter {
    CookieLocaleResolver resolver = new CookieLocaleResolver();


    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("classpath:i18n/messages");
        resourceBundleMessageSource.setCacheSeconds(1800);
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return resourceBundleMessageSource;
    }


    @Bean
    public LocaleResolver localeResolver() {
        getResolver().setDefaultLocale(new Locale("fr"));
        getResolver().setCookiePath("/");
        getResolver().setCookieName("cs-user-local");
        int ageInSeconds = 30 * 24 * 60 * 60;
        getResolver().setCookieMaxAge(ageInSeconds);
        return getResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        registry.addInterceptor(lci);
    }

    public CookieLocaleResolver getResolver() {
        return resolver;
    }


    public void setResolver(CookieLocaleResolver resolver) {
        this.resolver = resolver;
    }
}
