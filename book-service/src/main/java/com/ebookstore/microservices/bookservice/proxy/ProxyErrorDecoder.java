package com.ebookstore.microservices.bookservice.proxy;

import com.ebookstore.microservices.bookservice.exceptions.InvalidOrExpiredTokenException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ProxyErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            return new InvalidOrExpiredTokenException("Token is invalid or expired.");
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
