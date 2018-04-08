package com.project.test.unit;

import com.project.security.PasswordValidator;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by zen on 23/05/17.
 */
public class PasswordValidatorTest {


    protected PasswordValidator passwordValidator = new PasswordValidator();

    @Test
    public void testGoodPassword()
    {
        Assert.assertTrue(passwordValidator.validate("Gj7k@=):;,83"));
    }

    //--------- moins de 8 caracteres
    @Test
    public void testBadPassword() {
        Assert.assertFalse(passwordValidator.validate("piere"));
    }

    //--------- il manque des caracteres ou des lettres en majuscules
    @Test
    public void testBadPasswordAndSecurity() {
        Assert.assertFalse(passwordValidator.validate("987a654ze"));
    }

}
