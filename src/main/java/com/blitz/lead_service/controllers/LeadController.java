package com.blitz.lead_service.controllers;


import com.blitz.lead_service.domain.lead.Lead;
import com.blitz.lead_service.dtos.LeadDto;
import com.blitz.lead_service.services.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/lms/api/v1/lead")
@Tag(name = "Leads") // for api documentation
@RequiredArgsConstructor
public class LeadController {

     private final LeadService service;
     private static final Logger log = LoggerFactory.getLogger(LeadController.class);

    @Operation(description = "Uploads a .txt file to create multiple new leads in bulk.", responses = {
            @ApiResponse(description = "Returns a JSON object with a success message", responseCode = "200 Ok"),
            @ApiResponse(description = "Returns a JSON object with a generic error message if processing fails.", responseCode = "417 Expectation Failed")}
    )
    @PostMapping("/file")
    private ResponseEntity<Map<String, String>> processFile(@RequestParam("file") MultipartFile clientFile, HttpServletRequest request) throws IOException {

        Map<String, String> response = new HashMap<>();
        try {
            service.initAndProcessFile(clientFile, request);
            response.put("message", "File processing complete and leads created successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "An unexpected error occurred. Not to worry, this has been escalated.");
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "fetches all demo leads.", responses = {
            @ApiResponse(description = "Returns a list of ALL demo leads.", responseCode = "200 Ok"),
            @ApiResponse(description = "Returns an empty list if no demo leads are available.", responseCode = "404 Not found")}
    )
    @GetMapping("/demo/leads")
    private ResponseEntity<?> fetchAllDemoLeads() {
        List<Lead> demoLeads =  service.fetchAllDemoLeads();

        if (demoLeads.isEmpty()) {
            return new ResponseEntity<>("No demo leads available.",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(demoLeads,HttpStatus.OK);
    }

    // ---------------------- CREATE LEAD ----------------------
    @PostMapping
    @Operation(summary = "Create a new lead")
    public ResponseEntity<?> createLead(@RequestBody LeadDto dto) {
        try {
            Optional<LeadDto> savedLead = service.saveLead(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLead.get());
        } catch (Exception e) {
            log.error("Error creating lead: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // ---------------------- GET ALL LEADS ----------------------
    @GetMapping
    @Operation(summary = "Get all leads for a user")
    public ResponseEntity<?> getAllLeads(@RequestHeader("user-id") UUID userId) {
        List<LeadDto> leads = service.getAllLeads(userId);
        return ResponseEntity.ok(leads);
    }

    // ---------------------- GET LEAD BY ID ----------------------
    @GetMapping("/{leadId}")
    @Operation(description = "Fetch a lead by its ID")
    public ResponseEntity<?> getLeadById(@PathVariable UUID leadId) throws Exception {
        return service.getLeadById(leadId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Lead not found")));
    }


    // ---------------------- UPDATE LEAD ----------------------
    @PutMapping("/{leadId}")
    @Operation(summary = "Update a lead by ID")
    public ResponseEntity<?> updateLead(@PathVariable("leadId") UUID leadId, @RequestBody LeadDto dto) {
        try {
            Optional<LeadDto> updatedLead = service.updateLead(dto, leadId);

            if (updatedLead.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Lead not found for ID: " + leadId));
            }

            return ResponseEntity.ok(updatedLead.get());
        } catch (Exception e) {
            log.error("Error updating lead: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }



    // ---------------------- DELETE LEAD ----------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a lead by ID")
    public ResponseEntity<Map<String, String>> deleteLead(@PathVariable("id") UUID leadId) {
        try {
            service.deleteLead(leadId);
            return ResponseEntity.ok(Map.of("message", "Lead deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting lead: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lead not found"));
        }
    }
}
