package com.a2mac1.releasemanager.dto;

import jakarta.validation.constraints.*;

public record DeployRequest(

        @NotBlank(message = "Service name is required")
        @Size(max = 255, message = "Service name is too long")
        String name,

        @NotNull(message = "Service version is required")
        @Positive(message = "Version must be positive")
        Integer version

) {}