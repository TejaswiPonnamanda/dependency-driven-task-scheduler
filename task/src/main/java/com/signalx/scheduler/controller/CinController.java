package com.signalx.scheduler.controller;

import com.signalx.scheduler.dto.CinRequest;
import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.TaskStatus;
import com.signalx.scheduler.service.CinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cins")
public class CinController {

    @Autowired
    private CinService cinService;

    @PostMapping
    public ResponseEntity<Cin> createCin(@RequestBody CinRequest request) {
        // Pass only Cin details and dinIds to service
        Cin cin = new Cin(request.getName(), TaskStatus.PENDING, null, 0,null );
        Cin created = cinService.createCin(cin, request.getDinIds());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Cin>> getAllCins() {
        return ResponseEntity.ok(cinService.getAllCins());
    }

    @DeleteMapping("/{cinId}")
    public ResponseEntity<Void> deleteCin(@PathVariable Long cinId) {
        cinService.deleteCin(cinId);
        return ResponseEntity.noContent().build();
    }
}


