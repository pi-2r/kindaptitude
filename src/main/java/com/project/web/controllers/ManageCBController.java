package com.project.web.controllers;

import com.project.backend.persistence.domain.backend.User;
import com.project.backend.service.GetUserInformation;
import com.project.backend.service.I18NService;
import com.project.backend.service.UserService;
import com.project.security.CheckLuhnAlgorithmCB;
import com.project.security.Audit;
import com.project.utils.SendEmail;
import com.project.utils.StripeUtils;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Locale;

import static com.project.utils.RegexUtils.getValueWithRegex;
import static com.project.utils.RegexUtils.regexChecker;

/**
 * Created by zen on 10/08/17.
 */
@Controller
public class ManageCBController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAndCreditController.class);

    public static final String SUBSCRIPTION_AND_CREDIT = "subscriptionandcredit/subscription";
    public static final String MANAGE_CB = "subscriptionandcredit/managecb";
    public static final String MANAGE_CB_POST = "/managecb";
    public static final String DELETE_CB_POST = "/deleteCreditCard";


    //---- monthRegex
    final String monthRegex = "^[0-9]{2}$";
    final String yearRegex = "^[0-9]{4}$";
    final String cvcRegex = "^[0-9]{3}$";

    final static String EXP_YEAR = "\\,\"expYear\":(.*?)\\,\"";
    final static String BRAND = "\\,\"brand\":\"(.*?)\\\",\"";
    final static String EXP_MONTH = "\\,\"expMonth\":(.*?)\\,\"";
    final static String LAST4 = "\\\"last4\":\"(.*?)\\\",\"";

    @Autowired
    private GetUserInformation getUserInformation;
    @Autowired
    protected Audit logUser;
    @Autowired
    private I18NService i18NService;
    @Autowired
    public SendEmail sendEmail;
    @Autowired
    public StripeUtils stripeUtils;
    @Autowired
    private UserService userService;

    public static final String MESSAGE = "message";
    public static final String ADD_MESSAGE_KEY = "addCBMessage";
    public static final String EXP_YEAR_MESSAGE = "exp_year";
    public static final String BRAND_MESSAGE = "brand";
    public static final String EXP_MONTH_MESSAGE = "exp_month";
    public static final String LAST4_MESSAGE = "last4";


    @RequestMapping("/managecb")
    public String manageCb(HttpSession session,
                           Device device,
                           Model model, Locale locale,
                           @RequestParam(value = "addcb", required = false) String addcb,
                           @RequestParam(value = "deletecreditcarderror", required = false) String deletecreditcarderror,
                           @RequestParam(value = "companyname", required = false) String companyname,
                           @RequestParam(value = "regex", required = false) String regex,
                           @RequestParam(value = "creditcarderror", required = false) String creditcarderror,
                           @RequestParam(value = "luhncheck", required = false) String luhncheck,
                           @RequestParam(value = "delete", required = false) String delete) {

        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {

            if ("yes".equals(addcb)) {
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.addcbYes", locale));
            }

            if ("no".equals(addcb)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.addcbNo", locale));
            }

            if ("yes".equals(deletecreditcarderror)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.deleteError", locale));
            }

            if ("no".equals(companyname)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.noCompany", locale));
            }

            if ("no".equals(regex)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.noValidecb", locale));
            }

            if ("no".equals(luhncheck)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.noValidecb", locale));
            }

            if ("yes".equals(creditcarderror)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.creditCardError", locale));
            }

            if ("no".equals(delete)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("creditcardNumber.deleteNo", locale));
            }


            //----- cus_B8tj8NlzEipooN
            if (!"no".equals(session.getAttribute("stripeToken").toString())) {
                Customer cu = stripeUtils.getStripCustomerInformation(session.getAttribute("stripeToken").toString());
                String parseSource = readJsonFromStripe(cu);
                returnValue(parseSource, model);
            }
            return MANAGE_CB;
        } else {
            logUser.logNavigation(session, device, "managecb.html", "not allowed");
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = MANAGE_CB_POST, method = RequestMethod.POST)
    public String POST_manageCb(HttpSession session,
                                Device device,
                                Model model, Locale locale,
                                @RequestParam(value = "ccNumber", required = false) String ccNumber,
                                @RequestParam(value = "expMonth", required = false) String expMonth,
                                @RequestParam(value = "expYear", required = false) String expYear,
                                @RequestParam(value = "cvcNumber", required = false) String cvcNumber) {


        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            if (StringUtils.isEmpty(ccNumber) || StringUtils.isEmpty(expMonth) || StringUtils.isEmpty(expYear)
                    || StringUtils.isEmpty(cvcNumber) || (ccNumber.length() < 13) || (ccNumber.length() > 19)) {
                LOG.error("cb error: empty element ");
                return "redirect:/managecb?addcb=no";
            } else {

                if (!CheckLuhnAlgorithmCB.luhnCheck(ccNumber)) {
                    //---  luhncheck
                    return "redirect:/managecb?luhncheck=no";
                }

                if ((!regexChecker(monthRegex, expMonth)) || (!regexChecker(yearRegex, expYear)) || (!regexChecker(cvcRegex, cvcNumber))) {
                    return "redirect:/managecb?regex=no";
                }
                if (StringUtils.isEmpty(session.getAttribute("companyName").toString())) {
                    return "redirect:/managecb?companyname=no";
                }
                //---- Here we go :)
                String userInformation = session.getAttribute("companyName").toString() + "_userid:_"
                        + session.getAttribute("email").toString();

                //------ check if the customer is already presnent
                if (!"no".equals(session.getAttribute("stripeToken").toString())) {
                    Customer cu = stripeUtils.getStripCustomerInformation(session.getAttribute("stripeToken").toString());
                    try {
                        cu.delete();
                    } catch (AuthenticationException e) {
                        LOG.error(e.toString());
                        return "redirect:/managecb?deletecreditcarderror=yes";
                    } catch (InvalidRequestException e) {
                        LOG.error(e.toString());
                        return "redirect:/managecb?deletecreditcarderror=yes";
                    } catch (APIConnectionException e) {
                        LOG.error(e.toString());
                        return "redirect:/managecb?deletecreditcarderror=yes";
                    } catch (CardException e) {
                        LOG.error(e.toString());
                        return "redirect:/managecb?deletecreditcarderror=yes";
                    } catch (APIException e) {
                        LOG.error(e.toString());
                        return "redirect:/managecb?deletecreditcarderror=yes";
                    }
                    Long userId = Long.parseLong(session.getAttribute("userId").toString());
                    userService.updatestripeCustomerId(userId, "");
                }


                //--- call Strip in order to create credit card finger
                String token = stripeUtils.createToken(ccNumber, expMonth, expYear, cvcNumber, userInformation);

                if (token == null) {
                    return "redirect:/managecb?creditcarderror=yes";
                }
                Long userId = Long.parseLong(session.getAttribute("userId").toString());

                userService.updatestripeCustomerId(userId, token);
                //------ update session information
                User user = userService.findById(userId);
                getUserInformation.setAllInformationAboutUser(session, user);
                //----- send email

            }
            return "redirect:/managecb?addcb=yes";
        } else {
            logUser.logNavigation(session, device, "managecb.html", "not allowed");
            return UserController.USER_VIEW;
        }
    }

    @RequestMapping(value = DELETE_CB_POST, method = RequestMethod.POST)
    private String deleteCBPost(HttpSession session,
                                Device device,
                                @RequestParam(value = "yes", required = false) String yes) {

        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            if (!Boolean.valueOf(yes)) {
                LOG.error("cb error: empty element ");
                return "redirect:/managecb?delete=no";
            }

            //-------
            if (!"no".equals(session.getAttribute("stripeToken").toString())){
                Customer cu = stripeUtils.getStripCustomerInformation(session.getAttribute("stripeToken").toString());
                try {
                    cu.delete();
                } catch (AuthenticationException e) {
                    LOG.error(e.toString());
                } catch (InvalidRequestException e) {
                    LOG.error(e.toString());
                } catch (APIConnectionException e) {
                    LOG.error(e.toString());
                } catch (CardException e) {
                    LOG.error(e.toString());
                } catch (APIException e) {
                    LOG.error(e.toString());
                }

                Long userId = Long.parseLong(session.getAttribute("userId").toString());
                userService.updatestripeCustomerId(userId, "no");
                //------ update session information
                User user = userService.findById(userId);
                getUserInformation.setAllInformationAboutUser(session, user);

            } else {
                LOG.error("cb error: bad stripe id");
                return "redirect:/managecb?deletecreditcard=no";
            }
            return "redirect:/managecb?deletecreditcard=yes";
        } else {
            logUser.logNavigation(session, device, "deleteCreditCard.html", "not allowed");
            return UserController.USER_VIEW;
        }
    }

    private String readJsonFromStripe(Customer jsonFile) {

        JSONObject obj = new JSONObject(jsonFile);
        return obj.getJSONObject("sources").getJSONArray("data").toString();
    }

    private void returnValue(String data, Model model) {

        model.addAttribute(EXP_YEAR_MESSAGE, getValueWithRegex(EXP_YEAR, data));
        model.addAttribute(BRAND_MESSAGE, getValueWithRegex(BRAND, data));
        model.addAttribute(EXP_MONTH_MESSAGE, getValueWithRegex(EXP_MONTH, data));
        model.addAttribute(LAST4_MESSAGE, getValueWithRegex(LAST4, data));
    }
    public static final String GENERIC_ERROR_VIEW_NAME = "error/genericError";


    @ExceptionHandler({com.project.exceptions.StripeException.class})
    public ModelAndView signupException(HttpServletRequest request, Exception exception) {

        LOG.error("Request {} raised exception {}", request.getRequestURL(), exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
        mav.setViewName(GENERIC_ERROR_VIEW_NAME);
        return mav;
    }
}
