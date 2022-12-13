package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PayPalEndpoints {
    GET_ACCESS_TOKEN("/v1/oauth2/token"),
    GET_CLIENT_TOKEN("/v1/identity/generate-token"),
    ORDER_CHECKOUT("/v2/checkout/orders");

    private final String path;
    private final static Logger log = LoggerFactory.getLogger(PayPalEndpoints.class);

    PayPalEndpoints(String path) {
        this.path = path;
    }

    public static String createUrl(String baseUrl, PayPalEndpoints endpoint) {
        String url = baseUrl + endpoint.path;
        return url;
    }

    public static String createUrl(String baseUrl, PayPalEndpoints endpoint, String... params) {
        String url = baseUrl + String.format(endpoint.path, params);
        return url;
    }

}
