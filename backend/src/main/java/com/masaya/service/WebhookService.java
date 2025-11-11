package com.masaya.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masaya.domain.WebhookOutbox;
import com.masaya.model.Plant;
import com.masaya.repository.WebhookOutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WebhookService {

    private final WebhookOutboxRepository outboxRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 必ず 127.0.0.1 にしてテストする
    @Value("${app.webhook.watering-url:http://127.0.0.1:8081/webhooks/watering}")
    private String wateringUrl;

    public WebhookService(WebhookOutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // スケジューラから最初に呼ばれるほう
    public void sendWateringEvent(Plant plant) {
        WateringPayload payloadObj = new WateringPayload(
                plant.getId(),
                plant.getName(),
                plant.getSpecies(),
                LocalDateTime.now()
        );

        String json;
        try {
            json = objectMapper.writeValueAsString(payloadObj);
        } catch (JsonProcessingException e) {
            System.err.println("[WEBHOOK] JSON build failed: " + e.getMessage());
            saveOutbox(wateringUrl, "{}", "JSON_ERROR: " + e.getMessage());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>(json, headers);

        try {
            restTemplate.postForEntity(wateringUrl, req, String.class);
            // 成功なら特に何もしない
        } catch (RestClientException ex) {
            // ← ここが今回一番見たいところ
            System.err.println("[WEBHOOK] failed to POST to " + wateringUrl + " : " + ex.getMessage());
            saveOutbox(wateringUrl, json, ex.getMessage());
        }
    }

    private void saveOutbox(String url, String json, String error) {
        WebhookOutbox box = new WebhookOutbox();
        box.setTargetUrl(url);
        box.setPayload(json);
        box.setStatus("FAILED");
        box.setLastError(error);
        box.setNextRetryAt(LocalDateTime.now().plusMinutes(5));
        outboxRepository.save(box);
    }

    // 5分ごとの再送ジョブから呼ぶ
    public void resendPending() {
        List<WebhookOutbox> targets =
                outboxRepository.findByStatusInAndNextRetryAtBefore(
                        List.of("FAILED", "PENDING"),
                        LocalDateTime.now()
                );

        for (WebhookOutbox box : targets) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> req = new HttpEntity<>(box.getPayload(), headers);

            try {
                restTemplate.postForEntity(box.getTargetUrl(), req, String.class);
                box.setStatus("SENT");
                box.setLastError(null);
                box.setNextRetryAt(null);
                outboxRepository.save(box);
            } catch (RestClientException ex) {
                // ← ここにも出す
                System.err.println("[WEBHOOK/RETRY] failed to POST to "
                        + box.getTargetUrl() + " : " + ex.getMessage());
                box.setStatus("FAILED");
                box.setLastError(ex.getMessage());
                box.setNextRetryAt(LocalDateTime.now().plusMinutes(5));
                outboxRepository.save(box);
            }
        }
    }
    //内部
    public record WateringPayload(Long plantId,
                                  String name,
                                  String species,
                                  LocalDateTime detectedAt) {}
}
