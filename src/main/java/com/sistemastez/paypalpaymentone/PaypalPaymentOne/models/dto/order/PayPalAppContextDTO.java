package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.PaymentLandingPage;

public class PayPalAppContextDTO {
    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("landing_page")
    private PaymentLandingPage landingPage;
    @JsonProperty("return_url")
    private String returnUrl;
    @JsonProperty("cancel_url")
    private String cancelUrl;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public PaymentLandingPage getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(PaymentLandingPage landingPage) {
        this.landingPage = landingPage;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
