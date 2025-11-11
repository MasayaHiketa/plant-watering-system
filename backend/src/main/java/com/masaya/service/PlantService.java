package com.masaya.service;

import com.masaya.model.Plant;
import com.masaya.model.WateringLog;
import com.masaya.repository.PlantRepository;
import com.masaya.repository.WateringLogRepository;
import org.springframework.stereotype.Service;
import com.masaya.domain.AppUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final WateringLogRepository wateringLogRepository;

    public PlantService(PlantRepository plantRepository,
                        WateringLogRepository wateringLogRepository) {
        this.plantRepository = plantRepository;
        this.wateringLogRepository = wateringLogRepository;
    }

    public List<Plant> findByUserId(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    public Plant createForUser(Plant plant, AppUser user) {
        plant.setUser(user);   // ← Plantエンティティにuserフィールドを作っておく
        return plantRepository.save(plant);
    }


    public List<Plant> findAll() {
        return plantRepository.findAll();
    }

    public Optional<Plant> findById(Long id) {
        return plantRepository.findById(id);
    }

    public Plant create(Plant plant) {
        return plantRepository.save(plant);
    }

    public void delete(Long id) {
        plantRepository.deleteById(id);
    }

    // 💧ここが「水やり」メソッド
    public Optional<Plant> water(Long plantId, String note) {
        return plantRepository.findById(plantId).map(plant -> {
            // 植物の最終水やり日を今日に
            plant.setLastWateredAt(LocalDate.now());
            plantRepository.save(plant);

            // ログを追加
            WateringLog log = new WateringLog(plant, note);
            wateringLogRepository.save(log);

            return plant;
        });
    }

    public List<WateringLog> getLogs(Long plantId) {
        return wateringLogRepository.findByPlantIdOrderByWateredAtDesc(plantId);
    }

    //今すぐ“要水やり”の植物を見せて
    public List<Plant> getDueToday() {
        LocalDate today = LocalDate.now();
        return plantRepository.findAll().stream()
                .filter(plant -> {
                    if (plant.getLastWateredAt() == null) return true;
                    LocalDate nextWater = plant.getLastWateredAt()
                            .plusDays(plant.getWaterIntervalDays());
                    return !nextWater.isAfter(today);
                })
                .toList();
    }
    
    public List<Plant> getDueTodayForUser(Long userId) {
        LocalDate today = LocalDate.now();
        return plantRepository.findByUserId(userId).stream()
                .filter(plant -> {
                    if (plant.getLastWateredAt() == null) return true;
                    LocalDate nextWater = plant.getLastWateredAt()
                            .plusDays(plant.getWaterIntervalDays());
                    return !nextWater.isAfter(today);
                })
                .toList();
    }

}
