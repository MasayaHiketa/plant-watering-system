package com.masaya.service;

import com.masaya.model.WateringLog;
import com.masaya.repository.WateringLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WateringLogService {

    private final WateringLogRepository logRepository;

    public WateringLogService(WateringLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // 全部のログ（最新順）
    public List<WateringLog> getAllLogs() {
        return logRepository.findAllByOrderByWateredAtDesc();
    }

    // 特定植物のログ
    public List<WateringLog> getLogsByPlant(Long plantId) {
        return logRepository.findByPlant_IdOrderByWateredAtDesc(plantId);
    }
}
