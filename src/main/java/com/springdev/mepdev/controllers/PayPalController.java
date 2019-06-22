package com.springdev.mepdev.controllers;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.springdev.mepdev.models.Cours;
import com.springdev.mepdev.models.Utilisateur;
import com.springdev.mepdev.persistance.CoursRepository;
import com.springdev.mepdev.persistance.UtilisateurRepository;
import com.springdev.mepdev.services.PayPalClient;
import com.springdev.mepdev.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/paypal")
public class PayPalController {

    @Autowired
    @Qualifier("coursRepository")
    private CoursRepository coursRepository;

    @Autowired
    @Qualifier("userService")
    private UtilisateurService utilisateurService;

    @Autowired
    @Qualifier("userRepository")
    private UtilisateurRepository utilisateurRepository;

    @PostMapping(value = "/make/payment")
    public RedirectView makePayment(@RequestParam("sum") String sum
                                           , @RequestParam("payeeEmail") String payeeEmail
                , @RequestParam("cUrl") String cUrl, @RequestParam("sUrl") String sUrl,@RequestParam("coursId") Long coursId){
        String cancelUrl = "http://localhost:8080/paypal/"+cUrl;
        String successUrl = "http://localhost:8080/paypal/"+sUrl;


        PayPalClient payPalClient = new PayPalClient(cancelUrl,successUrl);
        Utilisateur utilisateur = utilisateurService.getCurrentUser();
        Cours coursAchete = coursRepository.getOne(coursId);
        utilisateur.addCoursAchetes(coursAchete);
        utilisateurRepository.save(utilisateur);

        return  new RedirectView(payPalClient.createPayment(sum,payeeEmail));
    }

    @PostMapping(value = "/complete/payment")
    public Map<String, Object> completePayment(HttpServletRequest request){
        PayPalClient payPalClient = new PayPalClient();
        return payPalClient.completePayment(request);
    }

    @GetMapping("/payment-success")
    public RedirectView successPay(@RequestParam("paymentId") String paymentId,@RequestParam("token") String token,@RequestParam("PayerID") String PayerID){
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PayerID);
        try {
            APIContext context = new APIContext(PayPalClient.clientId, PayPalClient.clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment != null){
                return new RedirectView("http://localhost:8080/cours-achetes");
            }
            else new RedirectView("http://localhost:8080/cours-achetes");
        } catch (PayPalRESTException e) {
            e.getDetails();
        }
        return new RedirectView("http://localhost:8080/cours-achetes");

    }
}
