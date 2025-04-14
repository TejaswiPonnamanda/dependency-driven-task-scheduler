package com.signalx.scheduler.controller;

import com.signalx.scheduler.dto.DinRequest;
import com.signalx.scheduler.model.Din;
import com.signalx.scheduler.service.DinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@RequestMapping("/api/dins")
@Tag(name = "Din Management", description = "APIs to manage Din entities and their dependencies")
public class DinController {

    private final DinService dinService;

    @Autowired
    public DinController(DinService dinService) {
        this.dinService = dinService;
    }

    @Operation(summary = "Create a new Din with optional Cin dependencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Din created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<Din> createDin(@RequestBody DinRequest request) {
        Din createdDin = dinService.createDin(request);
        return ResponseEntity.ok(createdDin);
    }

    @Operation(summary = "Get all Dins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Dins returned")
    })
    @GetMapping
    public ResponseEntity<List<Din>> getAllDins() {
        return ResponseEntity.ok(dinService.getAllDins());
    }

    @Operation(summary = "Delete a Din by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Din deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Din not found")
    })
    @DeleteMapping("/{dinId}")
    public ResponseEntity<Void> deleteDin(@PathVariable Long dinId) {
        dinService.deleteDin(dinId);
        return ResponseEntity.noContent().build();
    }
}

