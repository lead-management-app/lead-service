package com.blitz.lead_service.services;

import com.blitz.lead_service.domain.lead.Lead;
import com.blitz.lead_service.dtos.LeadDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadService {

    Optional<LeadDto> getLeadById(UUID leadId) throws Exception;

    Optional<LeadDto> saveLead(LeadDto dto) throws Exception;

    List<LeadDto> getAllLeads(UUID userId);

    Optional<LeadDto> updateLead(LeadDto leadDto, UUID leadId) throws Exception;

    void deleteLead(UUID leadId);

    void validateFile(File file);

    // after file has been vetted for accuracy, we create objects from the file contents.
    List<Lead> createLeadsFromFile(File file, UUID userId);

    void saveFileLeadsToDb(List<Lead> fileLeads);

    File initProcess(MultipartFile clientFile, HttpServletRequest request) throws IOException;

    void initAndProcessFile(MultipartFile clientFile, HttpServletRequest request) throws IOException;

    List<Lead> fetchAllDemoLeads() throws Exception;

}