package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientTokenDTO {
    @JsonProperty("client_token")
    private String clientToken;
    @JsonProperty("expires_in")
    private Long expiresIn;

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
