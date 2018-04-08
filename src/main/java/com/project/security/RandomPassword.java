package com.project.security;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by zen on 17/07/17.
 */
public class RandomPassword {

    public static String getRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String pwd = RandomStringUtils.random( length, characters );
        return pwd;
    }
}
