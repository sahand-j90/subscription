package com.example.subscription.services.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
public class UpdateSubscriberDto {

    @NotEmpty
    private UUID id;

    @NotEmpty
    private int version;

    @NotEmpty
    private String subscriberName;
}
