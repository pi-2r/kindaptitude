package com.project.utils;

import com.project.backend.service.StripeService;
import com.project.web.domain.frontend.ProAccountPayload;

import com.stripe.model.Customer;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zen on 30/07/17.
 */
@Service
public class StripeUtils {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(StripeUtils.class);
    @Autowired
    private StripeService StripeService;


    public static final String STRIPE_CARD_NUMBER_KEY = "number";
    public static final String STRIPE_EXPIRY_MONTH_KEY = "exp_month";
    public static final String STRIPE_EXPIRY_YEAR_KEY = "exp_year";
    public static final String STRIPE_CVC_KEY = "cvc";
    public static final String STRIPE_CARD_KEY = "card";
    public static final String STRIPE_DESCRIPTION = "description";




    /**
     * Given the card info provided by the user in the front-end, it returns a parameters map to obtain a Stripe token.
     * @param payload The info provided by the user during registration
     * @return A parameters map that can be used to obtain a Stripe token
     */
    public static Map<String, Object> extractTokenParamsFromSignupPayload(ProAccountPayload payload) {
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put(STRIPE_CARD_NUMBER_KEY, payload.getCardNumber());
        cardParams.put(STRIPE_EXPIRY_MONTH_KEY, Integer.valueOf(payload.getCardMonth()));
        cardParams.put(STRIPE_EXPIRY_YEAR_KEY, Integer.valueOf(payload.getCardYear()));
        cardParams.put(STRIPE_CVC_KEY, payload.getCardCode());
        tokenParams.put(STRIPE_CARD_KEY, cardParams);


        return tokenParams;
    }

    //--- create stripe token
    public  String  createToken(String ccNumber, String ccXpMonth, String ccXpYear,
                                           String ccCVC, String userinformation)
    {
        String stripeCustomerId = null;
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        Map<String, Object> customerParams = new HashMap<String, Object>();

        cardParams.put(STRIPE_CARD_NUMBER_KEY, ccNumber);
        cardParams.put(STRIPE_EXPIRY_MONTH_KEY, Integer.valueOf(ccXpMonth));
        cardParams.put(STRIPE_EXPIRY_YEAR_KEY, Integer.valueOf(ccXpYear));
        cardParams.put(STRIPE_CVC_KEY, ccCVC);
        tokenParams.put(STRIPE_CARD_KEY, cardParams);
        customerParams.put(STRIPE_DESCRIPTION, userinformation);

        stripeCustomerId = StripeService.createCustomer(tokenParams, customerParams);

        return stripeCustomerId;
    }

    //--- get information about stripeCustomer by token id
    public  Customer getStripCustomerInformation(String token) {

        return StripeService.getCustomerInformationBYToken(token);
    }


    public String createCharge(String amount, String currency, String description, String tok_mastercard) {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("description", description);
        chargeParams.put("source", tok_mastercard);
        StripeService.createCharge(chargeParams);
        return "toto";
    }

    public String createPlan(String amount, String interval, String name, String currency)
    {
        String planId =RandomStringUtils.random(64, false, true);

        Map<String, Object> planParams = new HashMap<String, Object>();
        planParams.put("amount",amount);
        planParams.put("interval",interval);
        planParams.put("name",name);
        planParams.put("currency",currency);
        planParams.put("id",planId);
        StripeService.createPlan(planParams);
        return planId;

    }

    public String createSubscription(String planId, String customerId){
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("plan", planId);

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("0", item);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customer", customerId);
        params.put("items", items);
        return StripeService.createSubscription(params);
    }


}
