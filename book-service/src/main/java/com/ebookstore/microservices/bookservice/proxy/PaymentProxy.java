package com.ebookstore.microservices.bookservice.proxy;

import com.ebookstore.microservices.bookservice.payload.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.payload.StripeChargeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name="payment-service", url="http://localhost:8100")
@FeignClient(name="payment-service")
public interface PaymentProxy {

    @PostMapping("/payment/create-payment-intent")
    PaymentConfirmationRequest createPaymentIntent(@RequestParam double price);

    @PostMapping("/payment/confirm-payment")
    ResponseEntity<String> confirmPayment(@RequestBody PaymentConfirmationRequest request);
    @PostMapping("/payment/charge")
    StripeChargeRequest charge(@RequestBody StripeChargeRequest StripeChargeRequest);
}
