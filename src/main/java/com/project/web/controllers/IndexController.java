package com.project.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zen on 14/05/17.
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/index")
    public String getInternationalPage() {
        return "index";
    }

    @RequestMapping(value="/testcookie", method = RequestMethod.GET)
    public void testCookie(HttpServletRequest request,
                           HttpServletResponse response) {

        Cookie cookie = new Cookie("token", "JGJGJGJGYGJHGJ");
        cookie.setMaxAge(265 * 24 * 60 * 60);  // (s)
        cookie.setPath("/");
        System.out.print("god");
        response.addCookie(cookie);   // response: HttpServletResponse

    }

}
