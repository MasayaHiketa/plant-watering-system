package com.masaya.repository;

import com.masaya.model.WateringLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WateringLogRepository extends JpaRepository<WateringLog, Long> {

    // ① 指定された plantId のログ（降順）
    List<WateringLog> findByPlant_IdOrderByWateredAtDesc(Long plantId);

    // ② 全部のログ（降順）
    List<WateringLog> findAllByOrderByWateredAtDesc();
}
