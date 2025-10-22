package com.blitz.lead_service.tourists;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private UUID id;

    private String name;

    private String email;

    private String role;

    private int lockedInd;

    private int enabledInd;

    private int deltaPasswordInd;

    private boolean isChangePassword() {
        return this.deltaPasswordInd == 1;
    }
}