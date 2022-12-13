package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.shared.LinkDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderStatus;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseDTO {
    private String id;
    private OrderStatus status;
    private List<LinkDTO> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<LinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDTO> links) {
        this.links = links;
    }
}
