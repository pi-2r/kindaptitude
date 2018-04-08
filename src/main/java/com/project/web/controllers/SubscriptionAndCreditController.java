package com.project.web.controllers;


import com.project.backend.persistence.domain.backend.Payment;
import com.project.backend.service.*;
import com.project.security.Audit;
import com.project.utils.SendEmail;
import com.project.utils.StripeUtils;
import com.project.utils.Tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by zen on 27/07/17.
 */
@Controller
public class SubscriptionAndCreditController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAndCreditController.class);

    public static final String SUBSCRIPTION_AND_CREDIT  = "subscriptionandcredit/subscription";
    public static final String MANAGE_CB  = "subscriptionandcredit/managecb";
    public static final String SUBSCRIPTION_AND_CREDIT_POST ="/subscriptionandcredit";


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
    @Autowired
    private Tools tools;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private MailClient mailClient;

    public static  final String MESSAGE="message";
    public static final String ADD_MESSAGE_KEY = "adduserMessage";

    @Value("${option.priceCRM}")
    private String priceCRM;
    @Value("${option.priceMRK}")
    private String priceMRK;
    @Value("${option.priceREC}")
    private String priceREC;
    @Value("${option.priceENT}")
    private String priceENT;
    @Value("${strip.period}")
    private String period;


    @Value("${option.priceSingleBlockEMAIL}")
    private String priceSingleBlockEMAIL;
    @Value("${option.priceSingleBlockSMS}")
    private String priceSingleBlockSMS;
    @Value("${payement.currency}")
    private String currency;


    @RequestMapping("/subscriptionandcredit")
    public String subscriptionAndcredit(HttpSession session,
                                        Device device,
                                        Model model, Locale locale,
                                        @RequestParam(value = "numberOption", required = false) String numberOption,
                                        @RequestParam(value = "emailPack", required = false) String emailPack,
                                        @RequestParam(value = "smsPack", required = false) String smsPack,
                                        @RequestParam(value = "levy", required = false) String levy,
                                        @RequestParam(value = "error", required = false) String error,
                                        @RequestParam(value = "nocreditcard", required = false) String nocreditcard,
                                        @RequestParam(value = "buy", required = false) String buy
                                        ) {
        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {

            if ("error".equals(numberOption)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.error.numberOption", locale));
            }
            if ("error".equals(emailPack)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.error.emailPack", locale));
            }
            if ("error".equals(levy)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.error.levy", locale));
            }
            if ("yes".equals(error)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.error.general", locale));
            }
            if ("yes".equals(nocreditcard)) {
                model.addAttribute(ADD_MESSAGE_KEY, "false");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.error.nocreditcard", locale));

            }
            if("yes".equals(buy)){
                model.addAttribute(ADD_MESSAGE_KEY, "true");
                model.addAttribute(MESSAGE, i18NService.getMessage("subscriptionandcredit.yes.buy", locale));

            }
            return SUBSCRIPTION_AND_CREDIT;

        } else {
            logUser.logNavigation(session, device, "subscriptionandcredit", "not allowed");
            return UserController.USER_VIEW;
        }
    }



    @RequestMapping(value = SUBSCRIPTION_AND_CREDIT_POST, method = RequestMethod.POST)
    public String POST_subscriptionAndcredit(HttpSession session,
                           Device device,
                           Model model, Locale locale,
                           @RequestParam(value="numberOption", required=false) String numberOption,
                           @RequestParam(value="kindOfChoise", required=false) String kindOfChoise,
                           @RequestParam(value="emailPack", required=false) String emailPack,
                           @RequestParam(value="smsPack", required=false) String smsPack,
                           @RequestParam(value="levy", required=false) String levy) {


        if (Boolean.valueOf(session.getAttribute("masterGroupe").toString())) {
            if ("true".equals(levy) || "false".equals(levy)) {
                String optionChoise="";
                    //---- check
                    try {
                        kindOfChoise = "CRM";
                        String period = "month";
                        Map<String, String> basket = new HashMap<String, String>();
                        if (Integer.parseInt(numberOption) < 0)   {
                            return "redirect:/subscriptionandcredit?numberOption=error";
                        }
                        if (Integer.parseInt(emailPack) < 0) {
                            return "redirect:/subscriptionandcredit?emailPack=error";
                        }
                        if (Integer.parseInt(smsPack) < 0) {
                            return "redirect:/subscriptionandcredit?smsPack=error";
                        }

                        //---- detect option choise
                        if ("CRM".equals(kindOfChoise)) {
                            optionChoise = priceCRM;
                        }
                        if("MRK".equals(kindOfChoise)) {
                            optionChoise = priceMRK;
                        }
                        if("REC".equals(kindOfChoise)) {
                            optionChoise = priceREC;
                        }
                        if("ENT".equals(kindOfChoise)) {
                            optionChoise = priceENT;
                        }

                        //---- get all information about buyer
                        basket.put(kindOfChoise,numberOption);
                        basket.put("emailPack",emailPack);
                        basket.put("smsPack",smsPack);

                        int totalOPtion = Integer.parseInt(numberOption) * Integer.parseInt(optionChoise);
                        int buyEmailPack = Integer.parseInt(emailPack) * Integer.parseInt(priceSingleBlockEMAIL);
                        int buySMSPack = Integer.parseInt(smsPack) * Integer.parseInt(priceSingleBlockSMS);
                        int totalAmount = (totalOPtion + buyEmailPack + buySMSPack) * 100 ;

                        //------- name information on Stripe
                        String name = session.getAttribute("companyName").toString() + "_" +tools.getDateToday();


                        //------ create plan
                        if ((session.getAttribute("stripeToken").toString() != "no") && ("true".equals(levy))){
                            // 1- create plan
                            String planId = stripeUtils.createPlan(String.valueOf(totalAmount), "month", name, currency);

                            // 2- create subscription
                            String subscriptionId = stripeUtils.createSubscription(planId,session.getAttribute("stripeToken").toString());

                            // 3- save data in database
                            saveInformationInDatabase(String.valueOf(totalAmount/100),
                                    period,
                                    subscriptionId,
                                    basket, session.getAttribute("companyId").toString());

                            // 4- send emails
                            String owner = session.getAttribute("firstName").toString() +" "+session.getAttribute("lastName").toString();
                            mailClient.sendEmailBuy(session.getAttribute("email").toString(),
                                    locale, owner, String.valueOf(totalAmount), period, kindOfChoise, numberOption,
                                    String.valueOf(totalOPtion), String.valueOf(buyEmailPack), String.valueOf(buySMSPack));



                        }else if((session.getAttribute("stripeToken").toString() == "no") && ("true".equals(levy)))
                        {
                            return "redirect:/subscriptionandcredit?nocreditcard=yes";
                        }

                        //----- create Payment only once
                        if ("false".equals(levy)) {

                        }

                        //------- add credit


                    } catch (NumberFormatException e) {
                        return "redirect:/subscriptionandcredit?error=yes";
                    }
                    return "redirect:/subscriptionandcredit?buy=yes";

            } else {
                return "redirect:/subscriptionandcredit?levy=error";
            }
        }
        else{
            logUser.logNavigation(session, device, "subscriptionandcredit", "not allowed");
            return UserController.USER_VIEW;
        }

    }

    private void saveInformationInDatabase(String totalAmount, String interval, String subscriptionId,
                                           Map<String, String> basket, String companyId){
        Payment payment =  new Payment();

        payment.setCreationDate(tools.getDateTodayTime());
        payment.setPrice(totalAmount);
        payment.setInterval(interval);
        payment.setIdCompany(companyId);
        payment.setSubscriptionId(subscriptionId);
        payment.setDetailsBuy(tools.mapToJson(basket));
        payment.setExpirationDate(tools.getDateAddOneMonth());
        paymentService.createPayment(payment);

    }



}
