package com.project.config;

import com.project.backend.service.UserSecurityService;
import com.project.web.controllers.*;
import com.project.web.restControllers.RestTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;


/**
 * Created by zen on 09/03/17.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private Environment env;

    /** The encryption salt */
    @Value("${saltPassword}")
    private String SALT;
    @Value("${strengthBCryptPasswordEncoder}")
    private int strengthBCryptPasswordEncoder;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strengthBCryptPasswordEncoder, new SecureRandom(SALT.getBytes()));
    }
    /** Public URL*/
    public static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/gif/**",
            "/images/**",
            "/fonts/**",
            "/",
            "/index*",
            "/badsession.*",
            "/404*",
            "/signup*",
            "/registrationCaptcha*",
            "/international*",
            "/about/**",
            "/contact/**",
            "/error/**/*",
            "/console/**",
            "/email/trigger",
            "/testcookie",
            "/error**",
            ForgotMyPasswordController.FORGOT_PASSWORD_URL_MAPPING,
            ForgotMyPasswordController.CHANGE_PASSWORD_PATH,
            SignupController.SUBSCRIPTION_VIEW_NAME,


    };

    /** Private URL*/
    public static final String[] PRIVATE_BASIC_MATCHERS = {
            UserController.USER_PROFILE,
            ProfileController.USER_PROFILE3,
            UserHistoryController.USER_HISTORY_NAVIGATION,
            YourUsersController.YOUR_USERS,
            SubscriptionAndCreditController.SUBSCRIPTION_AND_CREDIT,
            SubscriptionAndCreditController.MANAGE_CB,
            ManagePaymentController.MANAGE_PAYMENT_AND_MORE,
            ManageSubscriptionController.MANAGE_SUBSCRIPTION,
            RestTools.MENU_TOGGLE,
            RestTools.GET_2FA,
            RestTools.GET_NEW_USER,

    };

    /** Private URL*/
    public static final String[] PRIVATE_PRO_MATCHERS = {
            UserController.USER_PROFILE,
            ProfileController.USER_PROFILE3,
            UserHistoryController.USER_HISTORY_NAVIGATION,
            YourUsersController.YOUR_USERS,
            SubscriptionAndCreditController.SUBSCRIPTION_AND_CREDIT,
            SubscriptionAndCreditController.MANAGE_CB,
            ManagePaymentController.MANAGE_PAYMENT_AND_MORE,
            ManageSubscriptionController.MANAGE_SUBSCRIPTION,
            RestTools.MENU_TOGGLE,
            RestTools.GET_2FA,
            RestTools.GET_NEW_USER,

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if(activeProfiles.contains("dev")) {
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }
        http.headers().frameOptions().disable();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .antMatchers(PRIVATE_BASIC_MATCHERS).hasRole("USER")
                .antMatchers(PRIVATE_PRO_MATCHERS).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()

                .formLogin().loginPage("/login").defaultSuccessUrl("/check2fa")

                .failureUrl("/login?error").permitAll()
                .and()
                .logout().permitAll();

        http.sessionManagement()
                .maximumSessions(1).expiredUrl("/badsession");

        //http.addFilter(new RecaptchaAuthenticationFilter());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)  {
        try {
            auth.userDetailsService(userSecurityService)
                    .passwordEncoder(passwordEncoder());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
