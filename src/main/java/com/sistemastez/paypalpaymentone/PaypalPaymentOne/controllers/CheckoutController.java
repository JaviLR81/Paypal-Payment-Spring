package com.sistemastez.paypalpaymentone.PaypalPaymentOne.controllers;

import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dao.IOrderDao;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.capture.CaptureResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.PurchaseUnit;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.shared.MoneyDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.entity.Order;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderIntent;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderStatus;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.service.PayPalHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200", "*"} )
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final PayPalHttpClient payPalHttpClient;
    private final IOrderDao orderDAO;

    private final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    public CheckoutController(PayPalHttpClient payPalHttpClient, IOrderDao orderDAO) {
        this.orderDAO = orderDAO;
        this.payPalHttpClient = payPalHttpClient;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<OrderResponseDTO> checkout() throws Exception {
        MoneyDTO moneyDTO = new MoneyDTO();
        moneyDTO.setCurrencyCode("MXN");
        // moneyDTO.setValue("1245.12");
        moneyDTO.setValue(
                String.valueOf(payPalHttpClient.getRandomNumber(1, 1000)).concat(".".concat(String.valueOf(payPalHttpClient.getRandomNumber(1, 100)))));

        PurchaseUnit purchaseUnit = new PurchaseUnit();
        purchaseUnit.setAmount(moneyDTO);

        List<PurchaseUnit> list = List.of(purchaseUnit);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setIntent(OrderIntent.CAPTURE);
        orderDTO.setPurchaseUnits(list);

        var orderResponse = payPalHttpClient.createOrder(orderDTO);

        var entity = new Order();
        entity.setPaypalOrderId(orderResponse.getId());
        entity.setPaypalOrderStatus(orderResponse.getStatus().toString());
        entity.setAmount(new BigDecimal(moneyDTO.getValue()));
        var out = orderDAO.save(entity);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping(value = "/capture/{orderId}")
    public ResponseEntity<CaptureResponseDTO> paymentSuccess(@PathVariable String orderId) throws Exception {
        var captureResponse = payPalHttpClient.captureOrder(orderId);

        var out = orderDAO.findByPaypalOrderId(orderId);
        out.setPaypalOrderStatus(OrderStatus.COMPLETED.toString());
        orderDAO.save(out);

        return new ResponseEntity<>(captureResponse, HttpStatus.OK);
    }

    // TODO: This method is not handle the DTO pattern just with the intention of doesn't
    //  add complexity in the existing files that are mayority related to paypal development
    @GetMapping(value = "/list-orders")
    public ResponseEntity<?> listOrders() throws Exception {
        List<Order> orders = (List<Order>) orderDAO.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
