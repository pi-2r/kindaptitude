package com.project.test.unit;


import com.project.utils.Tools;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zen on 27/08/17.
 */

public class ToolsTest {

    Tools tools = new Tools();

    @Test
    public void testGoodNumber()
    {
        Map<String, String> test = new HashMap<>();
        test.put("user", "pierre");
        test.put("country", "France");
        test.put("town", "Paris");
        Assert.assertEquals("{\"country\":\"France\",\"town\":\"Paris\",\"user\":\"pierre\"}", tools.mapToJson(test));

    }
}
