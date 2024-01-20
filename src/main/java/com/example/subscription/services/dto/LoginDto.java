package com.example.subscription.services.dto;

import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Data
public class LoginDto {

    @NotEmpty(message = Messages.NOT_EMPTY)
    private String username;

    @NotEmpty(message = Messages.NOT_EMPTY)
    private String password;

}
