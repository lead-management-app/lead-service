package com.blitz.lead_service.mapper;

import com.blitz.lead_service.domain.lead.Lead;
import com.blitz.lead_service.dtos.LeadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface LeadMapper {

    @Mapping(target = "status", expression = "java(lead.getStatus().name())")
    LeadDto leadToLeadDto(Lead lead);

    @Mapping(target = "status", expression = "java(com.blitz.lead_service.domain.status.Status.valueOf(dto.getStatus()))")
    Lead leadDtoToLead(LeadDto dto);
}