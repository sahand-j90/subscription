package com.example.subscription.exceptions.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationItem {

    private String propertyName;

    private String message;

}
