package com.blitz.lead_service.config;

import com.blitz.lead_service.domain.lead.Lead;
import com.blitz.lead_service.domain.status.Status;
import com.blitz.lead_service.repo.LeadRepo;
import com.blitz.lead_service.tourists.clients.UserClient;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static com.blitz.lead_service.utils.IConstants.*;

@Configuration
@RequiredArgsConstructor
public class LeadsDataSeeder {


    Faker faker = new Faker(new Locale("en"));
    private final UserClient userClient;
    private final LeadRepo leadRepo;
    private final Logger log = LoggerFactory.getLogger(LeadsDataSeeder.class);

    @Bean
    public CommandLineRunner loadDummyLeads() throws Exception {

        return args -> {
            if (demoEntitiesExist()) {

                // TODO; WRAP RESILIENCY AROUND THIS.
                // fetch all user ids.
                List<UUID> demoUsersIds;
                try {
                    demoUsersIds = userClient.getDemoUserIds();
                } catch (Exception e) {
                    log.error("Error occurred while fetching demo user ids for creation of leads.");
                    throw new Exception("Could not create demo leads: user ids not available.");
                }

                Random random = new Random();

                // create dummy leads for each user id.
                demoUsersIds.forEach(userId -> {
                    // random number of leads(less than 500) for each user id
                    for (int count = 0; count < random.nextInt(500); count++) {

                        String companyName = faker.company().name();
                        String phone = faker.phoneNumber().phoneNumber();
                        String email = faker.internet().emailAddress(companyName.replaceAll("\\s+",".").toLowerCase());

                        if (leadRepo.findByName(companyName).isEmpty()) {
                            leadRepo.save(Lead.builder()
                                        .userId(userId)
                                        .name(companyName)
                                        .phoneNumber(phone)
                                        .email(email)
                                        .status(getStatus())
                                        .demoInd(1)
                                        .build()
                            );
                        }
                    }
                });
                System.out.println("✅ Dummy leads loaded successfully.");
            }
        };
    }

    private boolean demoEntitiesExist() {
        return leadRepo.isDemoLeadsExists() == 1;
    }

    private Status getStatus() {
        String[] status = new String[]
            {NEW_LEAD, FIRST_CALL, FOLLOW_UP, PROPOSAL_SENT, NEGOTIATION, CLOSED_WON, CLOSED_LOST};

        Random random = new Random();
        int statusVal = random.nextInt(status.length);
        return Status.valueOf(status[statusVal]);
    }
}