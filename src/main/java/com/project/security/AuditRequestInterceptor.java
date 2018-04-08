package com.project.security;

/**
 * Created by zen on 12/11/17.
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.backend.service.GetUserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Component
public class AuditRequestInterceptor extends HandlerInterceptorAdapter {

    // Define the log object for this class
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    private HttpSession session;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final Date today = new Date();
    /**
     * This is not a good practice to use sysout. Always integrate any logger
     * with your application. We will discuss about integrating logger with
     * spring boot application in some later article
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object object) throws Exception {


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object object, ModelAndView model) throws Exception {

        //---- check if user is admin
        Boolean isAdmin = request.isUserInRole("ADMIN");
        session.setAttribute("isAdmin", isAdmin);

        String doubleAuthValue = null;
        try {
            doubleAuthValue = session.getAttribute("valide2fa").toString();
        }catch (Exception ex){
            //ex.printStackTrace();
        }

        if(doubleAuthValue != null) {
            if (!"/check2fa.html".equals(request.getRequestURI().toString())) {
                if (getUserInformation.getUserId() != null) {
                    if (!Boolean.valueOf(doubleAuthValue)) {
                        response.sendRedirect("/check2fa.html?error=true");
                    }
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
            throws Exception {
        Device currentDevice = DeviceUtils.getCurrentDevice(request);

        String url= request.getScheme() + "://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getRequestURI() +
                request.getQueryString();

        if (getUserInformation.getUserId() != null) {
            //---- check if user is admin
            Boolean isAdmin = request.isUserInRole("ADMIN");
            session.setAttribute("isAdmin", isAdmin);
            LOG.info("userId: {}, email: {}, device: {}, userAgent: {}, page: {}, ipddress: {}, today: {}",
                    getUserInformation.getUserId(),
                    getUserInformation.getClientEmail(),
                    getUserInformation.getDevice(currentDevice),
                    getUserInformation.getBrowserInfo(),
                    url,
                    getUserInformation.getClientIP(),
                    dateFormat.format(today));
        }
        else {
            LOG.info("device: {}, userAgent: {}, page: {}, ipddress: {}, today: {}",
                    getUserInformation.getDevice(currentDevice),
                    getUserInformation.getBrowserInfo(),
                    url,
                    getUserInformation.getClientIP(),
                    dateFormat.format(today));
        }
    }
}
