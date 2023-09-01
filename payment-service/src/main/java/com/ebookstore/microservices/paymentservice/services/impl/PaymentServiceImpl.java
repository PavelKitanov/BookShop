package com.ebookstore.microservices.paymentservice.services.impl;

import com.ebookstore.microservices.paymentservice.dto.PaymentConfirmationRequest;
import com.ebookstore.microservices.paymentservice.dto.StripeChargeDto;
import com.ebookstore.microservices.paymentservice.dto.StripeTokenDto;
import com.ebookstore.microservices.paymentservice.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

    @Override
    public StripeChargeDto createCharge(StripeChargeDto chargeRequest) {

        try
        {
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
            chargeParams.put("currency","USD");
            chargeParams.put("description", "Payment for id  " + chargeRequest.getAdditionlInfo().getOrDefault("ID_TAG",""));
            chargeParams.put("source", chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getAdditionlInfo());
            chargeParams.put("metadata", metaData);

            Charge charge = Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if(charge.getPaid()){
                chargeRequest.setChargeid(charge.getId());
                chargeRequest.setSuccess(true);
            }

            return chargeRequest;
        } catch (StripeException e) {
            throw new RuntimeException("Unable to process the charge", e);
        }
    }

    @Override
    public StripeTokenDto createCardToken(StripeTokenDto stripeTokenDto){

        try{
            Map<String, Object> card = new HashMap<>();


            card.put("number", stripeTokenDto.getCardNumber());
            card.put("exp_month", stripeTokenDto.getExpMonth());
            card.put("exp_year", stripeTokenDto.getExpYear());
            card.put("cvc", stripeTokenDto.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);

            Token token = Token.create(params);
            if(token != null && token.getId() != null) {
                stripeTokenDto.setSuccess(true);
                stripeTokenDto.setToken(token.getId());
            }
            return stripeTokenDto;
        } catch (StripeException e) {
            throw new RuntimeException("Unable to get token: " + e);
        }
    }

    @Override
    public PaymentIntent createPaymentIntent(double price) throws StripeException {

        return PaymentIntent.create(new PaymentIntentCreateParams.Builder()
                    .setCurrency("usd")
                    .setAmount((long) (price * 100L))  // Amount in cents (10.00 USD)
                    .setPaymentMethod("pm_card_visa")
                    .build());
    }

    @Override
    public void confirmPayment(PaymentConfirmationRequest request) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());

        PaymentIntentConfirmParams confirmParams = new PaymentIntentConfirmParams.Builder()
                .setPaymentMethod(request.getPaymentMethodId())
                .setOffSession(true)  // Set this to false if you're confirming on the client side
                .setReceiptEmail("pavel.kitanov.oo@gmail.com")
                .build();

        paymentIntent.confirm(confirmParams);
    }
}
