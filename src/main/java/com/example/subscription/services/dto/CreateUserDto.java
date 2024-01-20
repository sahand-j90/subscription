package com.example.subscription.services.dto;

import com.example.subscription.enums.UserAuthorityEnum;
import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Data
public class CreateUserDto {

    @NotNull(message = Messages.NOT_EMPTY)
    private String username;

    @NotNull(message = Messages.NOT_EMPTY)
    private String password;

    @NotNull(message = Messages.NOT_EMPTY)
    private Boolean enabled;

    @NotEmpty(message = Messages.NOT_EMPTY)
    private List<UserAuthorityEnum> authorities;
}
