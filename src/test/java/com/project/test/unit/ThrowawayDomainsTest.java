package com.project.test.unit;

import com.project.security.ThrowawayDomains;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zen on 16/06/17.
 */
public class ThrowawayDomainsTest {

    @Autowired
    protected ThrowawayDomains throwawayDomains;

    @Test
    public void testThrowawayDomains() {
        Assert.assertTrue(throwawayDomains.getInstance().isThrowawayDomains("pierre.therrode@yopmail.com"));
        Assert.assertTrue (throwawayDomains.getInstance().isThrowawayDomains("pierre.therrode@gmail.com"));
        Assert.assertFalse(throwawayDomains.getInstance().isThrowawayDomains("pierre.therrode@kindaptitude.com"));
    }
}
