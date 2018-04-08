package com.project.backend.service;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * Created by zen on 30/07/17.
 */
@Service
public class StripeService {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(StripeService.class);

    @Value("${strip.secret.key}")
    private String stripeKey;

    /**
     * Creates a Stripe customer and returns the Stripe customer id
     * @param tokenParams The credit card details to obtain a token. These will never be stored in the DB
     * @param customerParams The parameters which identify the customer
     * @return The stripe customer id which can then be used to perform billing operations at a later stage
     * @throws com.project.exceptions.StripeException If an error occurred while interacting with Stripe
     */
    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {

        Stripe.apiKey = stripeKey;

        String stripeCustomerId = null;
        try {
            Token token = Token.create(tokenParams);
            customerParams.put("source", token.getId());
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();
        } catch (AuthenticationException e) {
            LOG.error("An authentication exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("An invalid request exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            LOG.error("An API connection exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("A Credit Card exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("An API exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        }
        return stripeCustomerId;
    }

    public Customer getCustomerInformationBYToken(String stripeCustomerId)
    {
        Customer cu = null;
        try {
            cu = Customer.retrieve(stripeCustomerId);

            } catch (AuthenticationException e) {
                LOG.error("An authentication exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (InvalidRequestException e) {
                LOG.error("An invalid request exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (APIConnectionException e) {
                LOG.error("An API connection exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (CardException e) {
                LOG.error("A Credit Card exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (APIException e) {
                LOG.error("An API exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            }
        return cu;
    }

    public void createCharge(Map<String, Object> chargeParams){

        try {
            Charge.create(chargeParams);
        } catch (AuthenticationException e) {
            LOG.error("An authentication exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("An invalid request exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            LOG.error("An API connection exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("A Credit Card exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("An API exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        }
    }


    public void createPlan(Map<String, Object> params) {

            Stripe.apiKey = stripeKey;
            try {
                Plan.create(params);
            } catch (AuthenticationException e) {
                LOG.error("An authentication exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (InvalidRequestException e) {
                LOG.error("An invalid request exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (APIConnectionException e) {
                LOG.error("An API connection exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (CardException e) {
                LOG.error("A Credit Card exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            } catch (APIException e) {
                LOG.error("An API exception occurred while creating the Stripe customer", e);
                throw new com.project.exceptions.StripeException(e);
            }

    }

    public String createSubscription(Map<String, Object> params){
        Stripe.apiKey = stripeKey;
        Subscription subscription = null;
        try {
            subscription = Subscription.create(params);
        } catch (AuthenticationException e) {
            LOG.error("An authentication exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("An invalid request exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            LOG.error("An API connection exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("A Credit Card exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("An API exception occurred while creating the Stripe customer", e);
            throw new com.project.exceptions.StripeException(e);
        }

        return subscription.getId();
    }
}
