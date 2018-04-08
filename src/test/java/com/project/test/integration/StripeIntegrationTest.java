package com.project.test.integration;
import com.project.DevopsbuddyApplication;
import com.project.backend.service.StripeService;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Created by zen on 30/07/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class StripeIntegrationTest {

    /** The application logger. */
    private static final Logger LOG = LoggerFactory.getLogger(StripeIntegrationTest.class);

    public static final String TEST_CC_NUMBER = "4242424242424242";

    public static final int TEST_CC_EXP_MONTH = 1;

    public static final String TEST_CC_CVC_NBR = "314";

    @Autowired
    private StripeService stripeService;

    @Value("${strip.secret.key}")
    private String stripeKey;

    @Before
    public void init() {
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }

    @Test
    public void createStripeCustomer() throws Exception {

        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", TEST_CC_NUMBER);
        cardParams.put("exp_month", TEST_CC_EXP_MONTH);
        cardParams.put("exp_year", LocalDate.now(Clock.systemUTC()).getYear() + 1);
        cardParams.put("cvc", TEST_CC_CVC_NBR);
        tokenParams.put("card", cardParams);

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Customer for test@example.com");

        String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
        assertThat(stripeCustomerId, is(notNullValue()));

        Customer cu = Customer.retrieve(stripeCustomerId);
        cu.delete();

    }
}
