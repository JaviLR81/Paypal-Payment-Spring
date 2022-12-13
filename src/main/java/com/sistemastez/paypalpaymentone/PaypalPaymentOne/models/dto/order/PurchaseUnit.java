package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order;


import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.shared.MoneyDTO;

public class PurchaseUnit {
    private MoneyDTO amount;

    public MoneyDTO getAmount() {
        return amount;
    }

    public void setAmount(MoneyDTO amount) {
        this.amount = amount;
    }
}
