package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderIntent;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private OrderIntent intent;
    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;
    @JsonProperty("application_context")
    private PayPalAppContextDTO applicationContext;

    public OrderIntent getIntent() {
        return intent;
    }

    public void setIntent(OrderIntent intent) {
        this.intent = intent;
    }

    public List<PurchaseUnit> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(List<PurchaseUnit> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }

    public PayPalAppContextDTO getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(PayPalAppContextDTO applicationContext) {
        this.applicationContext = applicationContext;
    }
}
