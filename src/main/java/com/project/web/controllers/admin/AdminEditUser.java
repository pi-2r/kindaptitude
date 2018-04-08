package com.project.web.controllers.admin;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.UserService;
import com.project.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zen on 03/12/17.
 */
@Controller
public class AdminEditUser {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(AdminEditUser.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Tools tools;

    public static final String MANAGE_USER_VIEW = "admin/adminEditUser";
    public static final String ERROR_PAGE = "error/genericError";
    public static final String USER_VIEW = "user/home";


    @RequestMapping("/adminEditUser")
    public String UserHome(HttpSession session, HttpServletRequest request, Model model)
    {
        if (request.isUserInRole("ADMIN")) {
            //----get value parameter and escape special characters
            try {
                String idVAlue = request.getParameter("id");
                //tools.escapeMetaCharacters(request.getParameter("id"))
                long id = Long.parseLong(idVAlue);
                UserInformation(id, model);
                return MANAGE_USER_VIEW;
            }catch (Exception ex){
                return ERROR_PAGE;
            }
        }else
        {
            return USER_VIEW;
        }
    }

    private Model UserInformation(long idUser, Model model){
        //------ update session information
        User user = userService.findById(idUser);
        return model.addAttribute("user", user);
    }
}
