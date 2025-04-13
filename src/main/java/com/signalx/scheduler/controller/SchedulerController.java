package com.signalx.scheduler.controller;

import com.signalx.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {


    @Autowired
    private SchedulerService schedulerService;


    @PostMapping("/run")
    public ResponseEntity<String> runScheduler() {
        schedulerService.scheduleTasks();
        return ResponseEntity.ok("Scheduling executed successfully.");
    }
}
