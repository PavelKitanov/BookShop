package com.ebookstore.microservices.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentConfirmationRequest {

    private String paymentIntentId;
    private String paymentMethodId;
}
