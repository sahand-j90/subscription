package com.example.subscription.services.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubscriptionDto {

    @NotNull
    private UUID id;

    @NotNull
    private int version;

    private LocalDate from;

    private LocalDate to;
}
