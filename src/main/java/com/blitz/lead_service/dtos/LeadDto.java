package com.blitz.lead_service.dtos;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LeadDto {

    private UUID id;

    private UUID userId;

    private String name;

    private String phoneNumber;

    private String email;

    private String status;

    private int demoInd;

    public boolean isDemoEntity() {
        return this.demoInd == 1;
    }

}