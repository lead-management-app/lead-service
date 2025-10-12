package com.blitz.lead_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
            contact = @Contact(name = "Kelvin Iseh", email = "kelviniseh25@gmail.com"),
            description = "OpenApi documentation for Lead Management System - Lead Service",
            title = "API Documentation for LMS AUTH Service"),
        servers = {
            @Server(description = "Test ENV", url = "http://localhost:9002"),
            @Server(description = "Cloud Gateway Port", url = "http://localhost:9090")
        }
)
public class OpenApiConfig {}