package com.ebookstore.microservices.paymentservice.web;

import com.ebookstore.microservices.paymentservice.dto.PaymentConfirmationRequest;
import com.ebookstore.microservices.paymentservice.dto.StripeChargeDto;
import com.ebookstore.microservices.paymentservice.dto.StripeTokenDto;
import com.ebookstore.microservices.paymentservice.services.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/card/token")
    @ResponseBody
    public StripeTokenDto createCardToken(@RequestBody StripeTokenDto stripeTokenDto){
        return paymentService.createCardToken(stripeTokenDto);
    }

    @PostMapping("/charge")
    @ResponseBody
    public StripeChargeDto charge(@RequestBody StripeChargeDto stripeChargeDto){
        return paymentService.createCharge(stripeChargeDto);
    }

    @PostMapping("/create-payment-intent")
    public PaymentConfirmationRequest createPaymentIntent(@RequestParam double price) {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(price);
            PaymentConfirmationRequest request = new PaymentConfirmationRequest(paymentIntent.getId(), paymentIntent.getPaymentMethod());
            return request;
        } catch (StripeException e) {
            return null;
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestBody PaymentConfirmationRequest request) {

        try {
            paymentService.confirmPayment(request);
            return ResponseEntity.ok("Payment confirmed successfully.");
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error confirming payment.");
        }
    }
}
