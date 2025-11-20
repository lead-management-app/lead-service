package com.blitz.lead_service.domain.status;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Status {

    NEW_LEAD("New Lead"),
    FIRST_CALL("First Call"),
    FOLLOW_UP("Follow Up"),
    PROPOSAL_SENT("Proposal Sent"),
    NEGOTIATION("Negotiation"),
    CLOSED_WON("Closed(Won)"),
    CLOSED_LOST("Closed(Lost)");

    private final String description;

    private Status(String description){
        this.description = description;
    };
}
