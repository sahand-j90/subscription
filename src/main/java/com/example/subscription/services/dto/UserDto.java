package com.example.subscription.services.dto;

import com.example.subscription.enums.UserAuthorityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String username;

    private int version;

    private Date createdAt;

    private Date updatedAt;

    boolean enabled;

    private List<UserAuthorityEnum> authorities;

}
