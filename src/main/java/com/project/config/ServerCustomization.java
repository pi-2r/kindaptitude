package com.project.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
//import org.springframework.boot.context.embedded.ErrorPage;


/**
 * Created by zen on 09/06/17.
 */

public class ServerCustomization extends ServerProperties {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {

        super.customize(container);
       /* container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                "/error/404.html"));
        container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
                "/jsp/500.jsp"));
        container.addErrorPages(new ErrorPage("/error/error.html"));
        */
    }

}
