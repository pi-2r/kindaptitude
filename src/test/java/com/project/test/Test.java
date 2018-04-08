package com.project.test;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.stripe.model.Charge;

/**
 * Created by zen on 30/07/17.
 */
public class Test {
    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    private String planId;
    private String stripeCustomerId;
    private String stripeToken;
    private final String USER_AGENT = "Mozilla/5.0";


    public static void main(String[] args) {
        Test test = new Test();
        test.GeoIP2City();
        //test.createToken();
        //test.createCharge();
        //Customer cu = stripeUtils.getStripCustomerInformation("cus_B8tj8NlzEipoo");
        //System.out.println(cu);
        /*System.out.println("=========> createToken: ok");
        test.createPlan();
        System.out.println("=========> createPlan: ok");
        test.createSubscription();
        System.out.println("=========> createSubscription: ok");
        */
    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://psych.prophet.rocks/v2/graph/profile";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("Origin", "chrome-extension://alikckkmddkoooodkchoheabgakpopmg");
        post.setHeader("authorization", "Psych UswoluKKIrwFsPzwikdDrBRWZCUrgvnItowdRLoeHgDnAWOvcEIPzYIwbeibyqlq");
        post.setHeader("x-prophet-version", "2.5.19.157");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Content-Type", "application/json;charset=UTF-8");


        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
        urlParameters.add(new BasicNameValuePair("cn", ""));
        urlParameters.add(new BasicNameValuePair("locale", ""));
        urlParameters.add(new BasicNameValuePair("caller", ""));
        urlParameters.add(new BasicNameValuePair("num", "12345"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

    //---- create plan ----
    private void createPlan() {
        Stripe.apiKey = "sk_test_dKuWpPJRbwso15an0UC1HeAJ";
        setPlanId(RandomStringUtils.random(64, false, true));

        Map<String, Object> planParams = new HashMap<String, Object>();
        planParams.put("amount",5000);
        planParams.put("interval","month");
        planParams.put("name","demo pierre");
        planParams.put("currency","eur");
        planParams.put("id",getPlanId());

        try {
            Plan.create(planParams);
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

    //------ Create a subscription --------
    private void createSubscription() {
        Stripe.apiKey = "sk_test_dKuWpPJRbwso15an0UC1HeAJ";

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("plan", getPlanId());

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("0", item);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("customer", getStripeCustomerId());
        params.put("items", items);
        try {
            Subscription subscription = Subscription.create(params);

            System.out.println(Subscription.retrieve(subscription.getId()));

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

    //--- create token ----
    private  void createToken()
    {
        Stripe.apiKey = "sk_test_dKuWpPJRbwso15an0UC1HeAJ";

        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", "4242424242424242");
        cardParams.put("exp_month", 7);
        cardParams.put("exp_year", 2018);
        cardParams.put("cvc", "314");
        tokenParams.put("card", cardParams);
        try {

            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("description", "Customer for test@example.com");
            //customerParams.put("plan", PlansEnum.PRO.getId());

            setStripeCustomerId(createCustomer(tokenParams, customerParams));
            System.out.println(getStripeCustomerId());

            Customer cu = Customer.retrieve(stripeCustomerId);

            System.out.println("=============+++> cu " + cu);

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
    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {

        Stripe.apiKey = "sk_test_dKuWpPJRbwso15an0UC1HeAJ";

        String stripeCustomerId = null;
        try {
            Token token = Token.create(tokenParams);
            System.out.println("===========================++>" + token.getId());
            setStripeToken(token.getId());
            customerParams.put("source", getStripeToken());
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

    public void createCharge(){
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        // Token is created using Stripe.js or Checkout!
        // Get the Payment token ID submitted by the form:
        //String token = request.getParameter("stripeToken");

        // Charge the user's card:
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", 1000);
        params.put("currency", "eur");
        params.put("description", "Example charge");
        //params.put("source", token);

        chargeParams.put("source", getStripeCustomerId());
        try {
            Charge charge = Charge.create(params);

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

    public String getPlanId() {return planId;}
    public void setPlanId(String planId) {this.planId = planId;}
    public String getStripeCustomerId() {return stripeCustomerId;}
    public void setStripeCustomerId(String stripeCustomerId) {this.stripeCustomerId = stripeCustomerId;}
    public String getStripeToken() {return stripeToken;}
    public void setStripeToken(String stripeToken) {this.stripeToken = stripeToken;}

    public void geoip() {
        try {
            // A File object pointing to your GeoIP2 or GeoLite2 database
            String ip = " 5.51.231.103";
            String dbLocation = "src/main/resources/GeoLite2/GeoLite2-City.mmdb";

            File database = new File(dbLocation);
            DatabaseReader dbReader = new DatabaseReader.Builder(database)
                    .build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            String countryName = response.getCountry().getName();
            String cityName = response.getCity().getName();
            String postal = response.getPostal().getCode();
            String state = response.getLeastSpecificSubdivision().getName();
            System.out.println("countryName: "+ countryName + ", cityName: " + cityName + ", postal: "+ postal +
                    ", state: "+ state);
        }catch(IOException ex) {
            ex.printStackTrace();
        }catch(GeoIp2Exception geo){
            geo.printStackTrace();
        }

    }

    public void GeoIP2City() {
        try {
        // A File object pointing to your GeoIP2 or GeoLite2 database
        File database = new File("src/main/resources/GeoLite2/GeoLite2-City.mmdb");

// This creates the DatabaseReader object, which should be reused across
// lookups.
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName("5.51.231.103");

// Replace "city" with the appropriate method for your database, e.g.,
// "country".
        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());            // 'US'
        System.out.println(country.getName());               // 'United States'
        System.out.println(country.getNames().get("zh-CN")); // '美国'

        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println(subdivision.getName());    // 'Minnesota'
        System.out.println(subdivision.getIsoCode()); // 'MN'

        City city = response.getCity();
        System.out.println(city.getName()); // 'Minneapolis'

        Postal postal = response.getPostal();
        System.out.println(postal.getCode()); // '55455'

        Location location = response.getLocation();
        System.out.println(location.getLatitude());  // 44.9733
        System.out.println(location.getLongitude()); // -93.2323
        }catch(IOException ex) {
            ex.printStackTrace();
        }catch(GeoIp2Exception geo){
            geo.printStackTrace();
        }
    }
}
