package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.Role;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.domain.backend.UserRole;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.backend.service.*;
import com.project.enums.PlansEnum;
import com.project.enums.RolesEnum;
import com.project.security.CheckValidId;
import com.project.security.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import java.util.*;

import static com.project.security.RandomPassword.getRandomPassword;

/**
 * Created by zen on 14/07/17.
 */
@Controller
public class YourUsersController {

    private static final Logger LOG = LoggerFactory.getLogger(YourUsersController.class);

    public static final String YOUR_USERS = "user/yourusers";
    public static final String ADD_USER = "/adduser", ADD_USER_GET = "/adduser";
    public static final String DELETE_USER_POST ="/deleteuser", DELETE_USER_GET ="/deleteuser";
    public static final String BLOCK_USER_POST ="/blockuser", BLOCK_USER_GET ="/blockuser";
    public static final String UNBLOCK_USER_POST ="/unblockuser", UNBLOCK_USER_GET ="/unblockuser";
    public static final String CHANGE_USER_TO_ADMIN_GROUP_POST = "/changeUserToAdminGroup", CHANGE_USER_TO_ADMIN_GROUP_GET ="/changeUserToAdminGroup";


    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    protected Audit logUser;
    @Autowired
    protected UserRepository user;
    @Autowired
    protected EmailValidator emailValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;
    @Autowired
    private I18NService i18NService;
    @Autowired
    private MailClient mailClient;
    @Autowired
    public CheckValidId checkValidId;
    public static  final String MESSAGE="message";
    public static final String ADD_MESSAGE_KEY = "adduserMessage";



    @RequestMapping("/yourusers")
    public String yourUsers(HttpSession session,
                            Device device,
                            Model model,Locale locale,
                            @RequestParam(value="adduser", required=false) String adduser,
                            @RequestParam(value="blockuser", required=false) String blockuser,
                            @RequestParam(value="unblockuser", required=false) String unblockuser,
                            @RequestParam(value="deleteuser", required=false) String deleteuser,
                            @RequestParam(value="noSendEmail", required=false) String noSendEmail)
    {



        if(Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            //adduser
            if ("yes".equals(adduser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.adduserYes", locale));
            }
            else if ("no".equals(adduser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.adduserNo", locale));
            }

            //blockuser
            if ("yes".equals(blockuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.blockuserYes", locale));
            }
            else if ("no".equals(blockuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.blockuserNo", locale));
            }

            //unblockuser
            if ("yes".equals(unblockuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.unblockuserYes", locale));
            }
            else if ("no".equals(unblockuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.unblockuserNo", locale));
            }

            //deleteuser
            if ("yes".equals(deleteuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.deleteuserYes", locale));
            }
            else if ("no".equals(deleteuser)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.deleteuserNo", locale));
            }
            if ("yes".equals(noSendEmail)){
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.noSendEmail", locale));
            }

            listAllUser(session.getAttribute("companyId").toString(),  model);
            return YOUR_USERS;
        }
        else
            return UserController.USER_VIEW;
    }

    /**
     * ADD user
     * @param session
     * @param device
     * @param model
     * @param locale
     * @param email
     * @return
     */
    @RequestMapping(value = ADD_USER, method = RequestMethod.POST)
    public String addUser(HttpSession session, Device device, Model model, Locale locale,
                          @RequestParam(value="email", required=false) String email,
                          @RequestParam(value="firstName", required=false) String firstName,
                          @RequestParam(value="lastName", required=false) String lastName) {
        String ownerUserId= session.getAttribute("userId").toString();
        listAllUser(session.getAttribute("companyId").toString(),  model);
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            if (!emailValidator.validate(email)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("signup.captcha.badEmail", locale));
                LOG.error("bad email: {} for userId {} ", email,ownerUserId);
                return YOUR_USERS;
            }

            if (userService.findByEmail(email) != null) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.existingEmail", locale));
                LOG.error("email {} allready  existe for the userId {} ", email, ownerUserId);
                return YOUR_USERS;
            }

            if (StringUtils.isEmpty(firstName)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badFirstName", locale));
                LOG.error("bad firstName: {}, for the user {}", firstName, ownerUserId);
                return YOUR_USERS;
            }
            if (StringUtils.isEmpty(lastName)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badLastName", locale));
                LOG.error("bad lastName: {}, for the user {}", lastName, ownerUserId);
                return YOUR_USERS;
            }




            //---- insert data in database
            User ownUser = user.findByEmail(session.getAttribute("email").toString());
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(lastName);
            user.setCompany(ownUser.getCompany());
            user.setBlockedByAdmin(false);
            user.setBlockedByOwnAdmin(false);
            //--- set random password
            String pwd = getRandomPassword(10);
            user.setPassword(pwd);
            user.setEnabled(true);
            user.setAlertArea(false);
            user.setStrongAUth(false);
            user.setLanguage(ownUser.getLanguage());
            user.setCountry(ownUser.getCountry());
            Set<UserRole> roles = new HashSet<>();
            roles.add(new UserRole(user, new Role(RolesEnum.USER)));
            user.setPlan(ownUser.getPlan());
            user.setMasterGroupe(false);

            String ownerUser= session.getAttribute("firstName").toString()+" "+session.getAttribute("lastName").toString();
            //--- send sample email
            Boolean flag = mailClient.addNewUser(email, locale, ownerUser, firstName, pwd);
            if(flag) {
                userService.createUser(user, PlansEnum.USER, roles, ownUser.getCompany());
            }else{
                return "redirect:/yourusers?noSendEmail=yes";
            }
            model.addAttribute(ADD_MESSAGE_KEY, "true");
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return "redirect:/yourusers?adduser=yes";
        } else {
            logUser.logNavigation(session, device, "adduser.html", "not allowed");
            return UserController.USER_VIEW;
        }
    }

    /**
     * display add new user
     * @param session
     * @param device
     * @param model
     * @param locale
     * @param email
     * @return page view
     */
    @RequestMapping(value = ADD_USER_GET, method = RequestMethod.GET)
    public String GET_addUser(HttpSession session, Device device, Model model, Locale locale,
                          @RequestParam(value="email", required=false) String email)
    {
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return YOUR_USERS;
        } else {
            logUser.logNavigation(session, device, "adduser.html", "not authorized");
            return UserController.USER_VIEW;
        }
    }

    /**
     * Delete a user
     * @param session
     * @param device
     * @param model
     * @param locale
     * @param userId
     * @return
     */
    @RequestMapping(value = DELETE_USER_POST, method = RequestMethod.POST)
    public String POST_deleteUser(HttpSession session, Device device, Model model, Locale locale,
                                  @RequestParam(value="userId", required=false) String userId) {

        String ownerUserId = session.getAttribute("userId").toString();
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            //--- check if string can be cast into long
            if(!checkValidId.validId(userId)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                LOG.error("ownerUser {} not valid userid: {}",ownerUserId, userId);
                return YOUR_USERS;
            }
            else {

                if (session.getAttribute("userId").toString().equals(userId)) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.highlanderRule", locale));
                    LOG.error("highlanderRule for ownerUser with  {} id ",ownerUserId);
                    return YOUR_USERS;
                }
                //--- Check if the user belongs to the same company
                long id = Long.parseLong(userId);
                User check =  userService.findById(id);
                long companyId = check.getCompany().getId();
                long ownCompanyId =  Long.parseLong(session.getAttribute("companyId").toString());

                if (companyId != ownCompanyId) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.notYourCompany", locale));
                    LOG.error("user with id: {}, is not your company {}",ownerUserId, companyId);
                    return YOUR_USERS;
                }
                userService.deleteById(Long.parseLong(userId));
            }

            model.addAttribute(ADD_MESSAGE_KEY, "true");
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return "redirect:/yourusers?deleteuser=yes";
        } else {
            logUser.logNavigation(session, device, "deleteuser.html", "not authorized");
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = DELETE_USER_GET, method = RequestMethod.GET)
    public String GET_deleteUser(HttpSession session, Device device, Model model, Locale locale) {
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return YOUR_USERS;
        } else {
            logUser.logNavigation(session, device, "deleteuser.html", "not authorized");
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = BLOCK_USER_GET, method = RequestMethod.GET)
    public String GET_blockUser(HttpSession session, Device device, Model model, Locale locale) {
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return YOUR_USERS;
        } else {
            logUser.logNavigation(session, device, "deleteuser.html", "not authorized");
            return UserController.USER_VIEW;
        }
    }

    /**
     * BLock a user
     * @param session
     * @param device
     * @param model
     * @param locale
     * @param userId
     * @return
     */
    @RequestMapping(value = BLOCK_USER_POST, method = RequestMethod.POST)
    public String POST_blockUser(HttpSession session, Device device, Model model, Locale locale,
                                 @RequestParam(value="userId", required=false) String userId) {

        String ownerUserId = session.getAttribute("userId").toString();
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            listAllUser(session.getAttribute("companyId").toString(),  model);
            //--- check if string can be cast into long
            if(!checkValidId.validId(userId)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                LOG.error("ownerUser {} not valid userid: {}",ownerUserId, userId);
                return YOUR_USERS;
            }
            else {

                if (session.getAttribute("userId").toString().equals(userId)) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.highlanderRule", locale));
                    LOG.error("highlanderRule for ownerUser with  {} id ",ownerUserId);
                    return YOUR_USERS;
                }
                //--- Check if the user belongs to the same company
                long id = Long.parseLong(userId);
                User check =  userService.findById(id);

                if (check !=null) {
                    long companyId = check.getCompany().getId();
                    long ownCompanyId = Long.parseLong(session.getAttribute("companyId").toString());

                    if (companyId != ownCompanyId) {
                        model.addAttribute(ADD_MESSAGE_KEY, "false");
                        model.addAttribute(MESSAGE, i18NService.getMessage("youruser.notYourCompany", locale));
                        LOG.error("user with id: {}, is not your company {}",ownerUserId, companyId);
                        return YOUR_USERS;
                    }
                    String ownerUser= session.getAttribute("firstName").toString()+" "+session.getAttribute("lastName").toString();

                    //--- send sample email
                    Boolean flag = mailClient.blockUser(check.getEmail(), locale, ownerUser,check.getFirstName());
                    if(flag) {
                        userService.blockUserbyOwnAdmin(id);
                    }else{
                        return "redirect:/yourusers?noSendEmail=yes";
                    }


                }else{
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                    LOG.error("badUserId: ", userId);
                    return YOUR_USERS;
                }
            }

            model.addAttribute(ADD_MESSAGE_KEY, "true");
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return "redirect:/yourusers?blockuser=yes";
        } else {
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = UNBLOCK_USER_POST, method = RequestMethod.POST)
    public String POST_UnBlockUser(HttpSession session, Device device, Model model, Locale locale,
                                   @RequestParam(value="userId", required=false) String userId) {

        String ownerUserId = session.getAttribute("userId").toString();
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            //--- check if string can be cast into long
            if(!checkValidId.validId(userId)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                LOG.error("ownerUser {} not valid userid: {}",ownerUserId, userId);
                return YOUR_USERS;
            }
            else {
                if (session.getAttribute("userId").toString().equals(userId)) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.highlanderRule", locale));
                    LOG.error("highlanderRule for ownerUser with  {} id ",ownerUserId);
                    return YOUR_USERS;
                }
                //--- Check if the user belongs to the same company
                long id = Long.parseLong(userId);
                User check =  userService.findById(id);
                long companyId = check.getCompany().getId();
                long ownCompanyId =  Long.parseLong(session.getAttribute("companyId").toString());

                if (companyId != ownCompanyId) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.notYourCompany", locale));
                    LOG.error("user with id: {}, is not your company {}",ownerUserId, companyId);
                    return YOUR_USERS;
                }
                String ownerUser= session.getAttribute("firstName").toString()+" "+session.getAttribute("lastName").toString();

                //--- send sample email
                Boolean flag = mailClient.unBlockUser(check.getEmail(), locale, ownerUser,check.getFirstName());
                if(flag) {
                    userService.unBlockUserbyOwnAdmin(id);
                }else{
                    return "redirect:/yourusers?noSendEmail=yes";
                }
            }

            model.addAttribute(ADD_MESSAGE_KEY, "true");
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return "redirect:/yourusers?unblockuser=yes";
        } else {
            logUser.logNavigation(session, device, "unblockuser.html", "not allowed");
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = CHANGE_USER_TO_ADMIN_GROUP_POST, method = RequestMethod.POST)
    public String POST_changeUserToAdminGroup (HttpSession session, Device device, Model model, Locale locale,
                                   @RequestParam(value="userId", required=false) String userId) {

        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            //--- check if string can be cast into long
            userId = "2";
            if(!checkValidId.validId(userId)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                LOG.error("userid is not a id: ", userId);
                return YOUR_USERS;
            }
            else {
                if (session.getAttribute("userId").toString().equals(userId)) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.highlanderRule", locale));
                    LOG.error("userid is not a id: ", userId);
                    return YOUR_USERS;
                }
                //--- Check if the user belongs to the same company
                long id = Long.parseLong(userId);
                User check =  userService.findById(id);
                long companyId = check.getCompany().getId();
                long ownCompanyId =  Long.parseLong(session.getAttribute("companyId").toString());

                if (companyId != ownCompanyId) {
                    model.addAttribute(ADD_MESSAGE_KEY, "false");
                    model.addAttribute(MESSAGE, i18NService.getMessage("youruser.badUserId", locale));
                    LOG.error("badUserId: ", userId);
                    return YOUR_USERS;
                }

                //--------- new user is admin
                userService.changeUserToAdminGroup(id);
                //--- call email sender
                String ownerUser= session.getAttribute("firstName").toString()+" "+session.getAttribute("lastName").toString();
                //mailClient.changeUserToAdminGroup(check.getEmail(), locale, ownerUser,check.getFirstName());
                //-------- admin user is simple usr
                userService.changeAdminGroupToUser(Long.parseLong(session.getAttribute("userId").toString()));
                //mailClient.changeAdminGroupToUser(check.getEmail(), locale, ownerUser,check.getFirstName());
            }

            //model.addAttribute(ADD_MESSAGE_KEY, "true");
            //listAllUser(session.getAttribute("companyId").toString(),  model);
            session.setAttribute("masterGroupe", false);
            return "redirect:/home?leaveroot=yes";
        } else
            return UserController.USER_VIEW;
    }



    @RequestMapping(value = UNBLOCK_USER_GET, method = RequestMethod.GET)
    public String GET_UnBlockUser(HttpSession session, Device device, Model model, Locale locale) {
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            listAllUser(session.getAttribute("companyId").toString(),  model);
            return YOUR_USERS;
        } else
            return UserController.USER_VIEW;
    }

    private Model listAllUser(String companyId, Model model) {
        List<User> yourUser = new ArrayList<>();
        yourUser =  user.findByCompany(Long.parseLong(companyId));
        return model.addAttribute("yourUsers", yourUser);
    }


}
