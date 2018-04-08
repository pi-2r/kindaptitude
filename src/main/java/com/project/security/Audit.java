package com.project.security;

import com.project.backend.service.GetUserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zen on 20/06/17.
 */
@Service
public class Audit {

    /** The application logger */
    // Define the log object for this class
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final Date today = new Date();

    @Autowired
    private GetUserInformation getUserInformation;
    //--------- user - sessionId/device
    private Map<String, List<String>> userNavigationInformation = new HashMap<String, List<String>>();//This is one instance of the  map you want to store in the above list.
    private List<String> arraylist1 = new ArrayList<String>();

    /**
     * Methode that allow to trace all the navigation of user
     * @param session
     * @param device
     * @param page
     * @param information
     */
    public void logNavigation(HttpSession session, Device device, String page, String information) {

        LOG.info("userId: {}, email: {}, device: {}, userAgent: {}, page: {}, sessionId: {}, ipddress: {}, today: {}, information: {}",
                getUserInformation.getUserId(),
                getUserInformation.getClientEmail(),
                getUserInformation.getDevice(device),
                getUserInformation.getBrowserInfo(),
                page,
                session.getId(),
                getUserInformation.getClientIP(),
                dateFormat.format(today),
                information);
    }

    @Deprecated
    private void  detectShareAccount(String user, String sessionId, Long creationTime, String ipaddress,  String device) {

        if(userNavigationInformation.get(user) == null) {
            //---- if the user is not logger, we create a profil
            setInformation(user, sessionId, ipaddress, creationTime, device);
        }else {
            arraylist1 = userNavigationInformation.get(user);
            for (int i=0, d=arraylist1.size(); i<d; i++) {
                if (i == 0) {
                    if(!sessionId.toString().equals(arraylist1.get(i))) {
                        LOG.info("Error:::: Share account with user: " +user + ", sessionid is different");
                        setInformation(user, sessionId, ipaddress, creationTime, device);
                    }
                }
                else if (i== 1) {
                    if(!ipaddress.equals(arraylist1.get(i))) {
                        LOG.info("Error:::: Share account with user: " +user + ", ipaddress is different");
                        setInformation(user, sessionId, ipaddress, creationTime, device);
                    }
                }
                else if (i== 2) {
                    if(!creationTime.toString().equals(arraylist1.get(i))) {
                        LOG.info("Error:::: Share account with user: " +user + ", creationTime is different");
                        setInformation(user, sessionId, ipaddress, creationTime, device);
                    }
                }
                else if (i== 3) {
                    if(!device.equals(arraylist1.get(i))) {
                        LOG.info("Error:::: Share account with user: " +user + ", device is different");
                        setInformation(user, sessionId, ipaddress, creationTime, device);
                    }
                }
            }
        }
    }

    @Deprecated
    private void setInformation(String user, String sessionId, String ipaddress, Long creationTime, String device){
        arraylist1.add(sessionId);
        arraylist1.add(ipaddress);
        arraylist1.add(creationTime.toString());
        arraylist1.add(device);
        userNavigationInformation.put(user, arraylist1);
    }
}
