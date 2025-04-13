package com.signalx.scheduler.controller;

import com.signalx.scheduler.dto.DinRequest;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.service.DinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dins")
public class DinController {

    private final DinService dinService;

    @Autowired
    public DinController(DinService dinService) {
        this.dinService = dinService;
    }

    @PostMapping
    public ResponseEntity<Din> createDin(@RequestBody DinRequest request) {
        Din createdDin = dinService.createDin(request);
        return ResponseEntity.ok(createdDin);
    }

    @GetMapping
    public ResponseEntity<List<Din>> getAllDins() {
        return ResponseEntity.ok(dinService.getAllDins());
    }

    @DeleteMapping("/{dinId}")
    public ResponseEntity<Void> deleteDin(@PathVariable Long dinId) {
        dinService.deleteDin(dinId);
        return ResponseEntity.noContent().build();
    }
}

