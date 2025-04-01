package com.rxnzero.spring.webhook.demo.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final String CLIENT_WEBHOOK_URL = "http://localhost:8081/webhook/callback";
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("웹훅 수신 데이터: " + payload);
        triggerClientWebhook(payload);
        return ResponseEntity.ok("웹훅 수신 성공");
    }

    private void triggerClientWebhook(Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        restTemplate.postForEntity(CLIENT_WEBHOOK_URL, request, String.class);
    }
}