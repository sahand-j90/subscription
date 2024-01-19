package com.example.subscription.services.dto;

import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
public class CreateSubscriberDto {

    @NotEmpty(message = Messages.NOT_EMPTY)
    private String subscriberName;
}
