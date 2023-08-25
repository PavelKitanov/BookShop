package com.ebookstore.microservices.paymentservice.services.impl;

import com.ebookstore.microservices.paymentservice.dto.StripeChargeDto;
import com.ebookstore.microservices.paymentservice.dto.StripeTokenDto;
import com.ebookstore.microservices.paymentservice.services.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String API_SECRET_KEY = "sk_test_51NiuZRHkw4d7BXiymy2R0QScjk5K2mFZDCfLIFeaMKm7WTIwxv85ePH5mYebEvq8FCfieGM5W7qPzEhvu9su2cgc00JGMWAEHr";

    @PostConstruct
    public void init(){
        Stripe.apiKey = API_SECRET_KEY;
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
}
