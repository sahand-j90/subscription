package com.example.subscription.services.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubscriberDto {

    @NotEmpty
    private UUID id;

    @NotEmpty
    private int version;

    @NotEmpty
    private String subscriberName;
}
