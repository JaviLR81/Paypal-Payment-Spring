package com.sistemastez.paypalpaymentone.PaypalPaymentOne.controllers;

import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dao.IOrderDao;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.capture.CaptureResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.PayPalAppContextDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.entity.Order;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.OrderStatus;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.PaymentLandingPage;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.service.PayPalHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping
    public ResponseEntity<OrderResponseDTO> checkout(@RequestBody OrderDTO orderDTO) throws Exception {
        var appContext = new PayPalAppContextDTO();
        appContext.setReturnUrl("http://localhost:8080/checkout/success");
        appContext.setBrandName("My brand");
        appContext.setLandingPage(PaymentLandingPage.BILLING);
        orderDTO.setApplicationContext(appContext);
        var orderResponse = payPalHttpClient.createOrder(orderDTO);

        var entity = new Order();
        entity.setPaypalOrderId(orderResponse.getId());
        entity.setPaypalOrderStatus(orderResponse.getStatus().toString());
        var out = orderDAO.save(entity);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping(value = "/success")
    public ResponseEntity<CaptureResponseDTO> paymentSuccess(HttpServletRequest request) throws Exception {
        var orderId = request.getParameter("token");
        var out = orderDAO.findByPaypalOrderId(orderId);
        out.setPaypalOrderStatus(OrderStatus.APPROVED.toString());
        orderDAO.save(out);

        var captureResponse = payPalHttpClient.captureOrder(orderId);

        return new ResponseEntity<>(captureResponse, HttpStatus.OK);
    }
}
