package com.springdev.mepdev.config;

import java.util.HashMap;
import java.util.Map;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PayPalConfig {

    public  static String clientId = "AT8DnxoJPAVLAgGzYC7vgdmw-pr27FobcR4_jVB4jf0zLMmLNbFgZIGN81j2LhfRaBBnm0rwpFcoz1rV";
    public  static String clientSecret = "EHMAkmBt3j93e-88OD6TB1eHAmwiK35jaoVKDmPFAHgBS_LazP1l7Y38mna9gDkMGDYQa-FNpN3kP2Ve";
    public static String mode="sandbox";

    @Bean
    public Map<String, String> paypalSdkConfig(){
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", mode);
        return sdkConfig;
    }

    @Bean
    public OAuthTokenCredential authTokenCredential(){
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean(name = "apiContext")
    public  APIContext apiContext() throws PayPalRESTException{
        APIContext apiContext = new APIContext(authTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }
}