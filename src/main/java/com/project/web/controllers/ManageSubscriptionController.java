package com.project.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zen on 06/09/17.
 */
@Controller
public class ManageSubscriptionController {

    /** The login view name */
    public  static String MANAGE_SUBSCRIPTION = "subscriptionandcredit/managesubscription";

    @RequestMapping("managesubscription")
    public String managesubscription()
    {
        return MANAGE_SUBSCRIPTION;
    }
}
