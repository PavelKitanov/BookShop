package com.ebookstore.microservices.paymentservice.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StripeChargeDto {

    private String stripeToken;
    private String name;
    private Double amount;
    private Boolean success;
    private String message;
    private String chargeid;
    private Map<String, Object> additionlInfo = new HashMap<>();
}
