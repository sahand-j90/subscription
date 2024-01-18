package com.example.subscription.services.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
public class CreateSubscriberDto {

    @NotEmpty
    private String subscriberName;
}
