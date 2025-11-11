package com.masaya.job;

import com.masaya.service.WebhookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WebhookRetryJob {

    private final WebhookService webhookService;

    public WebhookRetryJob(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    // 5分ごと
    @Scheduled(cron = "0 */5 * * * *")
    public void retry() {
        webhookService.resendPending();
    }
}
