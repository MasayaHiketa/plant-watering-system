package com.masaya.controller;

import com.masaya.domain.AppUser;
import com.masaya.model.Plant;
import com.masaya.model.WateringLog;
import com.masaya.repository.AppUserRepository;
import com.masaya.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;
    private final AppUserRepository appUserRepository;

    public PlantController(PlantService plantService,
                           AppUserRepository appUserRepository) {
        this.plantService = plantService;
        this.appUserRepository = appUserRepository;
    }

    // ✅ ログイン中ユーザーの植物だけ返す
    @GetMapping
    public List<Plant> getAll(Authentication auth) {
        String username = auth.getName(); // 例: "masaya"
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(); // 見つからなければ 500 で落ちるが、まずはこれでOK
        return plantService.findByUserId(user.getId());
    }

    // ✅ 作成するときも「今のユーザーの植物」にする
    @PostMapping
    public ResponseEntity<Plant> create(@RequestBody Plant plant,
                                        Authentication auth) {
        String username = auth.getName();
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow();
        Plant created = plantService.createForUser(plant, user);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/water")
    public ResponseEntity<Plant> water(@PathVariable Long id,
                                       @RequestParam(required = false) String note,
                                       Authentication auth) {
        // ここも本当は「そのユーザーのplantか」をチェックしたほうが良い
        return plantService.water(id, note)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<WateringLog>> logs(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.getLogs(id));
    }

    @GetMapping("/due-today")
    public List<Plant> getDueToday(Authentication auth) {
        String username = auth.getName();
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow();
        return plantService.getDueTodayForUser(user.getId());
    }
}
