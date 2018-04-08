package com.project.web.restControllers;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.UserService;
import com.project.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zen on 16/08/17.
 */
@Controller
@RequestMapping("tools/")
public class RestTools {

    private static final Logger LOG = LoggerFactory.getLogger(RestTools.class);
    public  final static String MENU_TOGGLE = "menutoggle";
    public  final static String GET_2FA = "2fa";
    public  final static String GET_NEW_USER = "getnewuser";


    @Autowired
    private Tools tools;
    @Autowired
    private UserService userService;

    @RequestMapping(value = MENU_TOGGLE,  method = RequestMethod.GET)
    public void greeting(HttpSession session, @RequestParam(value="data", required=false) String data) {

        //---- check toogle user by session information
        Boolean collapsedInformation = Boolean.valueOf(session.getAttribute("leftpanelCollapsed").toString());
        LOG.info(collapsedInformation.toString());
        //---- update data
        if(collapsedInformation) {
            LOG.info("leftpanelCollapsed =========> " + false);
            session.setAttribute("leftpanelCollapsed", false);
        }
        else {
            LOG.info("leftpanelCollapsed =========> " + true);
            session.setAttribute("leftpanelCollapsed", true);
        }
    }

    /**
     * Méthode REST qui permet de générer un token QRCODE lors de de la selection
     * @param session
     * @param data
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = GET_2FA,  method = RequestMethod.GET)
    public ResponseEntity<String> get_2fa(HttpSession session, @RequestParam(value="data", required=false) String data,
                                          HttpServletRequest request, HttpServletResponse response)
    {
        String urlToDisplay="";
        if (data!=null && !data.isEmpty()) {
            String userId = session.getAttribute("userId").toString();
            if ("true".equals(data)) {
                LOG.info("userId {}, has display 2fa", userId);
                Map<String, String> result2FA = new HashMap<>();
                String key ="";
                String userEmail = session.getAttribute("email").toString();


                result2FA =  tools.generate2FA(userEmail);
                for(Map.Entry<String, String> entry : result2FA.entrySet()) {
                     key = entry.getKey();
                    urlToDisplay = entry.getValue();
                }

                //--- save data into database
                userService.update2FA(Long.valueOf(userId), Boolean.TRUE, key, urlToDisplay);
                session.setAttribute("strongAUth", Boolean.TRUE);
                session.setAttribute("urlToDisplayQRCode", urlToDisplay);
                LOG.info("display url: " + urlToDisplay + ", userId: "+ userId);
            }else {
                    userService.update2FA(Long.valueOf(userId), Boolean.FALSE, "", urlToDisplay);
                    session.setAttribute("strongAUth", Boolean.FALSE);
                    session.setAttribute("urlToDisplayQRCode", "");
                    response.setStatus(HttpServletResponse.SC_OK);
            }
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return ResponseEntity.ok(urlToDisplay);
    }

    /**
     *  methode que permet de récupérer les informations sur les nouveaux users
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = GET_NEW_USER,  method = RequestMethod.GET)
    public ResponseEntity<String> aboutNewUser(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Boolean isAdmin = Boolean.valueOf(session.getAttribute("masterGroupe").toString());
        if(isAdmin) {
            List<User> newUser = userService.getAllNewUserByCompany(Long.valueOf(session.getAttribute("companyId").toString()));
            int count = 0;
            String finalResponse =  "";
            Map<String, String> result = new HashMap<>();
            List<String> userInfo = new ArrayList<String>();;
            for (User user : newUser) {
                count++;
                userInfo.add( user.getEmail());
            }
            if (count > 0) {
                result.put("allNewUser", String.valueOf(userInfo));
                result.put("countNewUser", String.valueOf(count));
                finalResponse = tools.mapToJson(result);
            }
            return ResponseEntity.ok(finalResponse);
        }else {
            return ResponseEntity.ok("no admin");
        }
    }

}
