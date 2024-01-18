package com.example.subscription.services.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
public class CreateSubscriptionDto {

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;

    @NotNull
    private UUID subscriberId;

}
