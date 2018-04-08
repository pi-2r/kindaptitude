package com.project.test.unit;

import com.project.DevopsbuddyApplication;
import com.project.backend.service.GeoIP2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by zen on 27/08/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
@WebAppConfiguration
public class GeoIP2CityTest {

    @Autowired
    GeoIP2 geoIP2;

    @Test
    public void getCountryByIP()
    {
        Assert.assertEquals("{\"countryName\":\"France\"}", geoIP2.GeoIp2ByCity("176.186.231.54"));
    }

    @Test
    public void getCountryWithNullIp()
    {
        Assert.assertNull(geoIP2.GeoIp2ByCity(""));
    }

    @Test
    public void getCountryWithParisIP()
    {
        Assert.assertEquals("{\"cityName\":\"Paris\",\"countryName\":\"France\",\"postal\":\"75017\",\"state\":\"Île-de-France\"}",
                geoIP2.GeoIp2ByCity("5.51.231.103"));
    }

    @Test
    public void getCountryWithParisBadIP()
    {
        Assert.assertEquals(null, geoIP2.GeoIp2ByCity(" 5.51.231.103"));
    }


}
