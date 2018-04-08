package com.project.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zen on 09/03/17.
 */
@Controller
public class PayloadController {

    private  static final String PLAY_LOAD_VIEW = "payload/payload";

    @RequestMapping("/payload")
    public String payload()
    {
        return PLAY_LOAD_VIEW;
    }


}
