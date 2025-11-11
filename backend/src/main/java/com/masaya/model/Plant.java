package com.masaya.model;

import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;
import com.masaya.domain.AppUser;
import lombok.Data;

@Entity
@Table(name = "plants")
@Data
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 植物の名前（例：書桌のサボテン）
    @Column(nullable = false)
    private String name;

    // 種類（例：cactus, monstera ...）なくてもOK
    private String species;

    // 最後に水をあげた日
    private LocalDate lastWateredAt;

    // 何日に1回水やりするか
    @Column(nullable = false)
    private Integer waterIntervalDays = 7;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    // ===== コンストラクタ =====
    public Plant() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public LocalDate getLastWateredAt() {
        return lastWateredAt;
    }

    public void setLastWateredAt(LocalDate lastWateredAt) {
        this.lastWateredAt = lastWateredAt;
    }

    public Integer getWaterIntervalDays() {
        return waterIntervalDays;
    }

    public void setWaterIntervalDays(Integer waterIntervalDays) {
        this.waterIntervalDays = waterIntervalDays;
    }
}
