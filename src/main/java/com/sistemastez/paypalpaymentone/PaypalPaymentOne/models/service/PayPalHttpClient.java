package com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.config.PaypalConfig;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.auth.AccessTokenResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.capture.CaptureResponseDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderDTO;
import com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.dto.order.OrderResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.sistemastez.paypalpaymentone.PaypalPaymentOne.models.enums.PayPalEndpoints.*;

@Service
public class PayPalHttpClient {

    private final HttpClient httpClient;
    private final PaypalConfig paypalConfig;
    private final ObjectMapper objectMapper;

    private final Logger log = LoggerFactory.getLogger(PayPalHttpClient.class);

    @Autowired
    public PayPalHttpClient(PaypalConfig paypalConfig, ObjectMapper objectMapper) {
        this.paypalConfig = paypalConfig;
        this.objectMapper = objectMapper;
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    public AccessTokenResponseDTO getAccessToken() throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl(paypalConfig.getBaseUrl(), GET_ACCESS_TOKEN)))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, encodeBasicCredentials())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var content = response.body();
        return objectMapper.readValue(content, AccessTokenResponseDTO.class);
    }

    public OrderResponseDTO createOrder(OrderDTO orderDTO) throws Exception {
        var accessTokenDto = getAccessToken();
        var payload = objectMapper.writeValueAsString(orderDTO);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl(paypalConfig.getBaseUrl(), ORDER_CHECKOUT)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenDto.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var content = response.body();
        return objectMapper.readValue(content, OrderResponseDTO.class);
    }

    public CaptureResponseDTO captureOrder(String orderId) throws Exception {
        var accessTokenDto = getAccessToken();

        String url = createUrl(paypalConfig.getBaseUrl(), ORDER_CHECKOUT) + "/" + orderId + "/capture";

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenDto.getAccessToken())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var content = response.body();
        return objectMapper.readValue(content, CaptureResponseDTO.class);
    }

    private String encodeBasicCredentials() {
        var input = paypalConfig.getClientId() + ":" + paypalConfig.getSecret();
        return "Basic " + Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
