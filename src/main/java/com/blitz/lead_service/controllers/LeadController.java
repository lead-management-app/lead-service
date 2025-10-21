package com.blitz.lead_service.controllers;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lms/api/v1/lead")
@Tag(name = "Leads") // for api documentation
@RequiredArgsConstructor
public class LeadController {

     private final LeadService service;
     private static final Logger log = LoggerFactory.getLogger(LeadController.class);

    @Operation(description = "Uploads a .txt file to create multiple new leads in bulk.", responses =
            {@ApiResponse(description = "Returns a JSON object with a success message", responseCode = "200 Ok"),
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
}
