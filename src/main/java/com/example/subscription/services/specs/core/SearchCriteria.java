package com.example.subscription.services.specs.core;

import lombok.*;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {

    private String key;

    private SearchOperation operation;

    private Object value;

    private String join;
}
