package com.example.subscription.services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 18.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberDto {

    private UUID id;

    private int version;

    private Date createdAt;

    private Date updatedAt;

    private String subscriberName;
}
