package com.example.subscription.services.dto;

import lombok.Data;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Data
public class ChangePasswordDto {

    private String username;

    private String oldPassword;

    private String newPassword;

}
