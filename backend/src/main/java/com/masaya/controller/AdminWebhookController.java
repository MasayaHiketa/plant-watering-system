// src/main/java/com/masaya/controller/AdminWebhookController.java
package com.masaya.controller;

import com.masaya.domain.WebhookOutbox;
import com.masaya.repository.WebhookOutboxRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/webhook-errors")
public class AdminWebhookController {

    private final WebhookOutboxRepository repository;

    public AdminWebhookController(WebhookOutboxRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WebhookOutbox> list() {
        // 最新100件だけ返すなども可
        return repository.findAll();
    }
}
