package com.project.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by zen on 26/04/17.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.project.backend.persistence.repositories")
@ComponentScan("com.project.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/.devKindaptitude/application-common.properties")
public class ApplicationConfig {
}
