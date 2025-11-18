package com.blitz.lead_service.services;

import com.blitz.lead_service.domain.lead.Lead;
import com.blitz.lead_service.domain.status.Status;
import com.blitz.lead_service.dtos.LeadDto;
import com.blitz.lead_service.mapper.LeadMapper;
import com.blitz.lead_service.repo.LeadRepo;
import com.blitz.lead_service.utils.IConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepo leadRepo;
    private final LeadMapper mapper;

    @Override
    public Optional<LeadDto> getLeadById(UUID leadId) {

        Optional<Lead> optLead = leadRepo.findById(leadId);

        return optLead.map(mapper::leadToLeadDto);
    }

    @Override
    public Optional<LeadDto> saveLead(LeadDto dto) throws Exception {
        Optional<Lead> optLead = leadRepo
                .findByNameOrEmailAllIgnoreCase(dto.getName(), dto.getEmail());

        if (optLead.isPresent()) {
            if (optLead.get().getName().equalsIgnoreCase(dto.getName())) {
                throw new Exception("Lead with name: " + optLead.get().getName() + " already exists.");
            } else if (optLead.get().getEmail().equalsIgnoreCase(dto.getEmail())) {
                throw new Exception("Lead with email: " + optLead.get().getEmail() + " already exists.");
            }
        }

        dto.setStatus(String.valueOf(Status.NEW_LEAD));

        return Optional.of(mapper.leadToLeadDto(leadRepo.save(mapper.leadDtoToLead(dto))));
    }

    @Override
    public List<LeadDto> getAllLeads(UUID userId) {
        return leadRepo.findAllByUserId(userId).stream()
                .map(mapper::leadToLeadDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeadDto> updateLead(LeadDto dto, UUID leadId) throws Exception {
        Optional<Lead> existingLeadOpt = leadRepo.findById(leadId);

        if (existingLeadOpt.isEmpty()) {
            return Optional.empty();
        }

        Lead existingLead = existingLeadOpt.get();

        //  Update only non-null fields from DTO
        if (dto.getName() != null) existingLead.setName(dto.getName());
        if (dto.getEmail() != null) existingLead.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingLead.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getStatus() != null) existingLead.setStatus(Status.valueOf(dto.getStatus()));
        if (dto.getUserId() != null) existingLead.setUserId(dto.getUserId());

        Lead savedLead = leadRepo.save(existingLead);
        return Optional.of(mapper.leadToLeadDto(savedLead));
    }

    @Override
    public void deleteLead(UUID leadId) {
        leadRepo.deleteById(leadId);
    }

    public void validateFile(File file) {
        // go through each line in the file validate for accuracy.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            // skip first and second line
            reader.readLine();
            reader.readLine();

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                // Split line into attributes
                String[] parts = line.split(",");
                if ((parts.length < 2) || (parts.length > 3)) {
                    throw new RuntimeException("Invalid line format:  Line " + lineNum +
                            " has more/less than required number of values.");
                }
                lineNum++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // after file has been vetted for accuracy, we create objects from the file contents.
    public List<Lead> createLeadsFromFile (File file, UUID userId) {
        List<Lead> leadList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            // skip first and second line
            bufferedReader.readLine();
            bufferedReader.readLine();


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String name;
                String email;
                String phone;

                String parts[] = line.split(",");

                // Parse attributes
                name = parts[0].trim();
                email = parts[1].trim();
                phone = parts[2].trim();
                String status = IConstants.NEW_LEAD;

                // check that lead doesn't already exist in db before saving.
                if (leadRepo.findByName(name).isEmpty()) {
                    var lead = Lead.builder()
                            .userId(userId)
                            .name(name)
                            .phoneNumber(phone)
                            .email(email)
                            .status(Status.valueOf(status))
                            .build();
                    leadList.add(lead);
                };
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return leadList;
    }

    @Override
    public void saveFileLeadsToDb(List<Lead> fileLeads) {
        for (Lead lead : fileLeads) {
            leadRepo.save(lead);
        }
    }

    public void initAndProcessFile(MultipartFile clientFile, HttpServletRequest request) throws IOException {
        File file = initProcess(clientFile, request);

        validateFile(file);

        List<Lead> leads = createLeadsFromFile(file, UUID.fromString(request.getHeader("user-id")));

        saveFileLeadsToDb(leads);
    };


    public File initProcess(MultipartFile clientFile, HttpServletRequest request) throws IOException {
        String filePath = request.getServletContext().getRealPath("");

        File uploadDir = new File(filePath + "uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdir();  // Creates the directory if it doesn't exist
        }

        // Get the original filename from the client
        String originalFilename = clientFile.getOriginalFilename();
        // Create a File object with the path where file is to be saved
        File file = new File(uploadDir, originalFilename);

        // Save the file to the specified directory
        clientFile.transferTo(file);

        return file;
    }

    public List<Lead> fetchAllDemoLeads() {
        return leadRepo.findAllDemoLeads();
    };
}