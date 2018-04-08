package com.project.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zen on 01/08/17.
 */
@Service
public class RegexUtils {


    /*public static void main(String args[]) {
        System.out.println(regexChecker("^[0-9]{2}$", "54"));
        System.out.println(regexChecker("^[0-9]{2}$", "8798798"));
        getValueWithRegex("\\\"exp_year\": (.*?)\\,", "\"sources\": { \"data\": [{\"address_city\": \"toto\",\"address_country\": null,address_line1\": null,\"address_line1_check\": null,\"exp_year\": 2018,");

    }*/

    public static  Boolean regexChecker(String regex, String searchInString) {
        Boolean flagReturn = false;
         Pattern pattern = Pattern.compile(regex);
         Matcher matcher = pattern.matcher(searchInString);

        while (matcher.find()) {
            flagReturn = true;
        }

        return flagReturn;
    }

    public static String getValueWithRegex(String regex, String searchInString){
        String value= null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(searchInString);

        while (matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }

    public static String getDomaineName(String regex, String searchInString){
        String value= null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(searchInString);

        while (matcher.find()) {
            value = matcher.group(0);
        }
        return value;
    }
}
