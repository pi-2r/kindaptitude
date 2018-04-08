package com.project.utils;

import com.project.security.TimeBasedOneTimePasswordUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zen on 15/08/17.
 */
@Service
public class Tools {
    // This is how to get today's date in Java
    Date today = new Date();

    @Value("${titleCompany}")
    private String titleCompany;
    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);

    public String getDateToday(){
        //formatting date in Java using SimpleDateFormat
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(today);
        return date;
    }

    public String getDateAddOneMonth(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dStartTime=dateFormatter.format(today);
        Date dateStartTime = null;
        try {
            dateStartTime = dateFormatter.parse(dStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateStartTime);
        cal.add(Calendar.MONTH, 1);
        return dateFormatter.format(cal.getTime());
    }

    public String getDateTodayTime(){
        //formatting date in Java using SimpleDateFormat
        SimpleDateFormat DATE_FORMAT =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = DATE_FORMAT.format(today);
        return date;
    }

    public String getToken(){
        return UUID.randomUUID().toString();
    }

    public String mapToJson(Map<String, String> data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return  json;
    }



    public Map<String, String> generate2FA(String userEmail){
        LOG.info("generate2FA for user: {}", userEmail);
        Map<String, String> content = new HashMap<>();
        String urlToDisplay = "";
        String base32Secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();

        StringBuilder QRCodeTitle = new StringBuilder(titleCompany+"(");
        // this is the name of the key which can be displayed by the authenticator program
        QRCodeTitle.append(userEmail).append(")");
        // generate the QR code
        //System.out.println("Image url = " + TimeBasedOneTimePasswordUtil.qrImageUrl(keyId, base32Secret));
        urlToDisplay = TimeBasedOneTimePasswordUtil.qrImageUrl(QRCodeTitle.toString(), base32Secret);
        // we can display this image to the user to let them load it into their auth program
        content.put(base32Secret, urlToDisplay);
        return content;
    }

    public String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&"};
        String outputString="";
        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                outputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
                inputString = outputString;
            }
        }
        return outputString;
    }
}
