// src/main/java/com/masaya/repository/WebhookOutboxRepository.java
package com.masaya.repository;

import com.masaya.domain.WebhookOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WebhookOutboxRepository extends JpaRepository<WebhookOutbox, Long> {

    // 再送対象を拾う
    List<WebhookOutbox> findByStatusInAndNextRetryAtBefore(
            List<String> statuses, LocalDateTime now);
}
