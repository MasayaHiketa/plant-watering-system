package com.masaya.controller;

import com.masaya.model.WateringLog;
import com.masaya.service.WateringLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watering")
public class WateringLogController {

    private final WateringLogService wateringLogService;

    public WateringLogController(WateringLogService wateringLogService) {
        this.wateringLogService = wateringLogService;
    }

    // 全部のログ一覧（新規）
    @GetMapping("/logs")
    public ResponseEntity<List<WateringLog>> getAllLogs() {
        return ResponseEntity.ok(wateringLogService.getAllLogs());
    }

    // 特定植物のログ一覧（必要なら残す）
    @GetMapping("/logs/{plantId}")
    public ResponseEntity<List<WateringLog>> getLogsByPlant(@PathVariable Long plantId) {
        return ResponseEntity.ok(wateringLogService.getLogsByPlant(plantId));
    }
}
