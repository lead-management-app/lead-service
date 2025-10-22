package com.blitz.lead_service.domain.lead;

import com.blitz.lead_service.domain.status.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Entity
@Table(name = "lms_leads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Lead {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(length = 36, columnDefinition =  "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", length = 36, columnDefinition =  "uuid", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "demo_ind", nullable = false)
    private int demoInd;

    public boolean isDemoEntity() {
        return this.demoInd == 1;
    }


}