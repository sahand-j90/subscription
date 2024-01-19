package com.example.subscription.services.dto;

import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
public class CreateSubscriptionDto {

    @NotNull(message = Messages.NOT_EMPTY)
    private LocalDate from;

    @NotNull(message = Messages.NOT_EMPTY)
    private LocalDate to;

    @NotNull(message = Messages.NOT_EMPTY)
    private UUID subscriberId;

}
