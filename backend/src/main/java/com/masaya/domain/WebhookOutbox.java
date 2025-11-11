package com.masaya.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "webhook_outbox")
@Data
public class WebhookOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JSONは長くなるのでtextで固定
    @Column(columnDefinition = "text")
    private String payload;

    // URLも長めにしておきたいなら text にしておいてよい
    @Column(name = "target_url", columnDefinition = "text")
    private String targetUrl;

    // PENDING / FAILED / SENT くらいで十分
    private String status;

    // エラーメッセージも長くなるのでtextで固定
    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    private LocalDateTime nextRetryAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // ====== getter / setter ======
}
