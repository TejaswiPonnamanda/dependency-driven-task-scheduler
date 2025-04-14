package com.signalx.scheduler.controller;

import com.signalx.scheduler.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @PostMapping("/run")
    @Operation(summary = "Run the scheduler", description = "Trigger the scheduler to execute tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scheduler executed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> runScheduler() {
        schedulerService.scheduleTasks();
        return ResponseEntity.ok("Scheduling executed successfully.");
    }
}
