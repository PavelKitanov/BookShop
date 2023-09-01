package com.ebookstore.microservices.bookservice.dto;

import lombok.Data;

@Data
public class PaymentConfirmationRequest {

    private String paymentIntentId;
    private String paymentMethodId;

    public PaymentConfirmationRequest(String paymentIntentId, String paymentMethodId) {
        this.paymentIntentId = paymentIntentId;
        this.paymentMethodId = paymentMethodId;
    }
}
