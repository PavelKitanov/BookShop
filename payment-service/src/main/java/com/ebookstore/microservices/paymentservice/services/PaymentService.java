package com.ebookstore.microservices.paymentservice.services;

import com.ebookstore.microservices.paymentservice.dto.PaymentConfirmationRequest;
import com.ebookstore.microservices.paymentservice.dto.StripeChargeDto;
import com.ebookstore.microservices.paymentservice.dto.StripeTokenDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {

    StripeChargeDto createCharge(StripeChargeDto stripeChargeDto);
    StripeTokenDto createCardToken(StripeTokenDto stripeTokenDto);

    PaymentIntent createPaymentIntent(double price) throws StripeException;

    void confirmPayment(PaymentConfirmationRequest request) throws StripeException;

}
