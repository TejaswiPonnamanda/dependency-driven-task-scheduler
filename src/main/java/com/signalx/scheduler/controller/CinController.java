package com.signalx.scheduler.controller;

import com.signalx.scheduler.dto.CinRequest;
import com.signalx.scheduler.model.Cin;
import com.signalx.scheduler.model.TaskStatus;
import com.signalx.scheduler.service.CinService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cins")
@Tag(name = "Cin Controller", description = "APIs for managing Cin entities")
public class CinController {

    @Autowired
    private CinService cinService;

    @PostMapping
    @Operation(summary = "Create a new Cin", description = "Creates a Cin and establishes relationships with Dins.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cin created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Cin> createCin(@RequestBody CinRequest request) {
        Cin cin = new Cin(request.getName(), TaskStatus.PENDING, null, 0, null);
        Cin created = cinService.createCin(cin, request.getDinIds());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    @Operation(summary = "Fetch all Cins", description = "Returns a list of all registered Cins.")
    public ResponseEntity<List<Cin>> getAllCins() {
        return ResponseEntity.ok(cinService.getAllCins());
    }

    @DeleteMapping("/{cinId}")
    @Operation(summary = "Delete a Cin", description = "Deletes a Cin by its ID and updates related dependencies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cin deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cin not found")
    })
    public ResponseEntity<Void> deleteCin(
            @Parameter(description = "ID of the Cin to delete") @PathVariable Long cinId) {
        cinService.deleteCin(cinId);
        return ResponseEntity.noContent().build();
    }
}


