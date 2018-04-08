package com.project.backend.service;

import com.project.utils.RegexUtils;
import com.project.utils.Tools;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zen on 27/08/17.
 */
@Service
public class GeoIP2 {

    @Value("${GeoIp2City.path}")
    private String GeoIp2City;

    @Autowired
    private Tools tools;
    @Autowired
    private  RegexUtils regexUtils;

    private static final String IPADDRESS_PATTERN = "^(?:(?:1\\d?\\d|[1-9]?\\d|2[0-4]\\d|25[0-5])\\.){3}(?:1\\d?\\d|[1-9]?\\d|2[0-4]\\d|25[0-‌​5])(?:[:]\\d+)?$";

    public String GeoIp2ByCity(String ipAdress){
        String result = null;

        if(regexUtils.regexChecker(IPADDRESS_PATTERN, ipAdress)) {
            Map<String, String> resultGeoIP = new HashMap<>();
            try {
                // A File object pointing to your GeoIP2 or GeoLite2 database
                String dbLocation = GeoIp2City;

                File database = new File(dbLocation);
                DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

                InetAddress ipAddress = InetAddress.getByName(ipAdress);
                CityResponse response = dbReader.city(ipAddress);

                //----- save information in map
                resultGeoIP.put("countryName", response.getCountry().getName());
                resultGeoIP.put("cityName", response.getCity().getName());
                resultGeoIP.put("postal", response.getPostal().getCode());
                resultGeoIP.put("state", response.getLeastSpecificSubdivision().getName());

                result = tools.mapToJson(resultGeoIP);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (GeoIp2Exception geo) {
                geo.printStackTrace();
            }
        }
        return result;
    }
}
