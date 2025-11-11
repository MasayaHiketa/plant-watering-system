package com.masaya.service;

import com.masaya.model.Plant;
import com.masaya.repository.PlantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.masaya.service.WebhookService;

import java.time.LocalDate;
import java.util.List;

@Service
public class WateringScheduleService {

    private static final Logger log = LoggerFactory.getLogger(WateringScheduleService.class);

    private final PlantRepository plantRepository;
    private final WebhookService webhookService;

    public WateringScheduleService(PlantRepository plantRepository,
                                   WebhookService webhookService) {
        this.plantRepository = plantRepository;
        this.webhookService = webhookService;
    }

    // 毎朝6時にチェック（cron: 秒 分 時 日 月 曜日）
    //@Scheduled(cron = "0 0 6 * * *")
    //During development
    @Scheduled(fixedRate = 60000) // 60秒に一回

    public void checkDuePlants() {
        LocalDate today = LocalDate.now();
        List<Plant> plants = plantRepository.findAll();

        for (Plant plant : plants) {
            boolean needsWater;
            if (plant.getLastWateredAt() == null) {
                needsWater = true;
            } else {
                LocalDate nextWater = plant.getLastWateredAt()
                        .plusDays(plant.getWaterIntervalDays());
                needsWater = !nextWater.isAfter(today);  // nextWater <= today
            }

            if (needsWater) {
                // 今はログに出すだけ。あとでWebhookに変える
                log.info("Plant '{}' needs watering today.", plant.getName());
                webhookService.sendWateringEvent(plant); // ← Webhook 送信
            }
        }
    }
}
