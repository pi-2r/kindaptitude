package com.project.web.controllers.admin;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.backend.service.*;
import com.project.security.Audit;
import com.project.security.CheckValidId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zen on 03/12/17.
 */
@Controller
public class AllUsersController {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(AllUsersController.class);

    public static final String MANAGE_USER_VIEW = "admin/manageUsers";
    public static final String USER_VIEW = "user/home";

    public static final String BLOCK_USER_POST ="/adminBlockuser", BLOCK_USER_GET ="/adminBlockuser";


    @Autowired
    protected UserRepository user;
    @Autowired
    protected Audit logUser;
    @Autowired
    protected EmailValidator emailValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private I18NService i18NService;
    @Autowired
    private MailClient mailClient;
    @Autowired
    public CheckValidId checkValidId;
    public static  final String MESSAGE="message";
    public static final String ADD_MESSAGE_KEY = "adduserMessage";


    @RequestMapping("/allUsers")
    public String UserHome(HttpSession session, HttpServletRequest httpServletRequest, Model model)
    {
        if (httpServletRequest.isUserInRole("ADMIN")) {
            listAllUser(model);
            return MANAGE_USER_VIEW;
        }else
        {
            return USER_VIEW;
        }
    }


    @RequestMapping(value = BLOCK_USER_GET, method = RequestMethod.GET)
    public String GET_blockUser(HttpSession session, Device device, Model model, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.isUserInRole("ADMIN")) {
            listAllUser(model);
            return MANAGE_USER_VIEW;
        }else
        {
            return USER_VIEW;
        }
    }

    /**
     * BLock a user
     * @param session
     * @param model
     * @param locale
     * @param userId
     * @return
     */
    @RequestMapping(value = BLOCK_USER_POST, method = RequestMethod.POST)
    public String POST_blockUser(HttpSession session, HttpServletRequest httpServletRequest, Model model, Locale locale,
                                 @RequestParam(value="userId", required=false) String userId) {

        if (httpServletRequest.isUserInRole("ADMIN")) {
            //--- check if string can be cast into long
            if(!checkValidId.validId(userId)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                return "redirect:/allUsers?badId=yes";
            }
            else {

                if (session.getAttribute("userId").toString().equals(userId)) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.highlanderRule", locale));
                    return "redirect:/allUsers?highlanderRule=yes";
                }
                //--- Check if the user belongs to the same company
                long id = Long.parseLong(userId);
                User check =  userService.findById(id);

                if (check !=null) {
                    long companyId = check.getCompany().getId();
                    long ownCompanyId = Long.parseLong(session.getAttribute("companyId").toString());

                    String ownerUser= session.getAttribute("firstName").toString()+" "+session.getAttribute("lastName").toString();

                    //--- send sample email
                    Boolean flag = mailClient.blockUser(check.getEmail(), locale, ownerUser,check.getFirstName());
                    if(flag) {
                        userService.blockUserbyAdmin(id);
                    }else{
                        return "redirect:/allUsers?noSendEmail=yes";
                    }
                }else{
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                    LOG.error("badUserId: ", userId);
                    return "redirect:/allUsers?badUserId=yes";
                }
            }

            model.addAttribute(ADD_MESSAGE_KEY, "true");
            listAllUser(model);
            return "redirect:/allUsers?blockuser=yes";
        } else {
            return USER_VIEW;
        }
    }

    private Model listAllUser(Model model) {
        List<User> yourUser = new ArrayList<>();
        yourUser =  user.findAllUser();
        return model.addAttribute("yourUsers", yourUser);
    }

}
