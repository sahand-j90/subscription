package com.example.subscription.exceptions.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@Data
@Builder
public class ErrorMessage {

    private int code;

    private String description;

    private List<ValidationItem> validationErrorItems;

    private String spanId;

}
