package com.masaya.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "watering_logs")
public class WateringLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // どの植物のログか
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    @JsonIgnore
    private Plant plant;

    // 水やりした日時
    @Column(nullable = false)
    private LocalDateTime wateredAt = LocalDateTime.now();

    // メモ（任意）
    private String note;

    public WateringLog() {}

    public WateringLog(Plant plant, String note) {
        this.plant = plant;
        this.note = note;
        this.wateredAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public LocalDateTime getWateredAt() {
        return wateredAt;
    }

    public void setWateredAt(LocalDateTime wateredAt) {
        this.wateredAt = wateredAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
