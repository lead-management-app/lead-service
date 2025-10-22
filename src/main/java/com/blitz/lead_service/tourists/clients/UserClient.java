package com.blitz.lead_service.tourists.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "lms-auth-service",
        path = "/lms/v1/auth/user"
)
public interface UserClient {

    @GetMapping("/demo/usersIds")
    List<UUID> getDemoUserIds();
}
