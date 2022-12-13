package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dao;

import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface IOrderDao extends CrudRepository<Order, Long> {
    Order findByPaypalOrderId(String paypalOrderId);
}
