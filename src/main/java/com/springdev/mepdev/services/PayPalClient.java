package com.springdev.mepdev.services;

import com.paypal.api.payments.*;
import com.paypal.base.exception.PayPalException;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.springdev.mepdev.config.PayPalConfig;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayPalClient {

    public static  String clientId = "AT8DnxoJPAVLAgGzYC7vgdmw-pr27FobcR4_jVB4jf0zLMmLNbFgZIGN81j2LhfRaBBnm0rwpFcoz1rV";
    public static String clientSecret = "EHMAkmBt3j93e-88OD6TB1eHAmwiK35jaoVKDmPFAHgBS_LazP1l7Y38mna9gDkMGDYQa-FNpN3kP2Ve";

    private  String PAYPAL_CANCEL_URL;
    private  String PAYPAL_SUCCESS_URL;


    public PayPalClient(){

    }

    public PayPalClient(String payPalCancelUrl, String payPalSuccessUrl){
       this.PAYPAL_CANCEL_URL = payPalCancelUrl;
        this.PAYPAL_SUCCESS_URL = payPalSuccessUrl;
        System.out.println("cancel = " + PAYPAL_CANCEL_URL);
    }

    public String createPayment(String sum,String payeeEmail){
        Map<String, Object> response = new HashMap<>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);

        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(PAYPAL_CANCEL_URL);
        redirectUrls.setReturnUrl(PAYPAL_SUCCESS_URL);
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            Map<String, String> sdkConfig = new HashMap<>();
            sdkConfig.put("mode", PayPalConfig.mode);
            OAuthTokenCredential authTokenCredential= new OAuthTokenCredential(PayPalConfig.clientId, PayPalConfig.clientSecret
            ,sdkConfig);
            System.out.println(authTokenCredential.getAccessToken());
            APIContext apiContext = new APIContext(authTokenCredential.getAccessToken());

            apiContext.setConfigurationMap(sdkConfig);
            createdPayment = payment.create(apiContext);
            System.out.println(apiContext);
            if(createdPayment!=null){
                List<Links> links = createdPayment.getLinks();
                for (Links link:links) {
                    if(link.getRel().equals("approval_url")){
                        redirectUrl = link.getHref();
                        System.out.println(redirectUrl);
                        break;
                    }
                    else{
                        System.out.println("Error not approved");
                    }
                }
                return redirectUrl;
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getDetails());
            e.getStackTrace() ;
            System.out.println("ERROR 6 = "+ e.getResponsecode());
            System.out.println("Error happened during payment creation!");
        }
        return "http://localhost:8080/";
    }

    public Map<String, Object> completePayment(HttpServletRequest req){
        Map<String, Object> response = new HashMap();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("PayerID"));
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }

}
