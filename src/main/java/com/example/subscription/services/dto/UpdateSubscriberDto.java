package com.example.subscription.services.dto;

import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotEmpty(message = Messages.NOT_EMPTY)
    private UUID id;

    @NotEmpty(message = Messages.NOT_EMPTY)
    private int version;

    @NotEmpty(message = Messages.NOT_EMPTY)
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = Messages.INVALID_UUID)
    private String subscriberName;
}
