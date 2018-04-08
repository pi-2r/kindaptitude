package com.project.test.unit;

import org.junit.Assert;
import org.junit.Test;

import static com.project.utils.RegexUtils.*;

/**
 * Created by zen on 03/08/17.
 */
public class RegexTest {

    @Test
    public void testGoodNumber()
    {
        Assert.assertTrue(regexChecker("^[0-9]{2}$", "54"));
    }
    @Test
    public void testBadNumber()
    {
        Assert.assertFalse (regexChecker("^[0-9]{2}$", "5qsklsqjlk"));
    }

    @Test
    public void getSpecificValue() {
        Assert.assertEquals("2018", getValueWithRegex("\\\"exp_year\": (.*?)\\,", "\"sources\": { \"data\": [{\"address_city\": \"toto\",\"address_country\": null,address_line1\": null,\"address_line1_check\": null,\"exp_year\": 2018,"));
    }

    @Test
    public void getDomaineNameTest() {
        Assert.assertEquals("kindaptitude", getDomaineName("(?<=@)[^.]+", "pierre@kindaptitude.com"));
    }
}
