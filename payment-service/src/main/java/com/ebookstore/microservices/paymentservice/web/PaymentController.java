package com.ebookstore.microservices.paymentservice.web;

import com.ebookstore.microservices.paymentservice.dto.StripeChargeDto;
import com.ebookstore.microservices.paymentservice.dto.StripeTokenDto;
import com.ebookstore.microservices.paymentservice.services.PaymentService;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
}
