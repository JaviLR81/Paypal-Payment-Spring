package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderIntent;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private OrderIntent intent;
    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;

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
}
