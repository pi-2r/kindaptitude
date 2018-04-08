package com.project.test.unit;

import com.project.security.WorstPassword;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by zen on 21/05/17.
 */

public class WorstPasswordUnit {
    @Autowired
    protected WorstPassword worstPassword;

    @Test
    public void testWorstPassword() {
        Assert.assertFalse(WorstPassword.getInstance().isWorstPassword("g7"));
        Assert.assertTrue(WorstPassword.getInstance().isWorstPassword("1234567"));
    }
}
