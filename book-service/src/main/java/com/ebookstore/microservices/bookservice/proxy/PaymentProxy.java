package com.ebookstore.microservices.bookservice.proxy;


import com.ebookstore.microservices.bookservice.dto.PaymentConfirmationRequest;
import com.stripe.model.PaymentIntent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="payment-service", url="http://localhost:8100/payment")
public interface PaymentProxy {

    @PostMapping("/create-payment-intent/{price}")
    public PaymentConfirmationRequest createPaymentIntent(@PathVariable double price);

    @PostMapping("/confirm-payment")
    public ResponseEntity<Object> confirmPayment(@RequestBody PaymentConfirmationRequest request);
}
