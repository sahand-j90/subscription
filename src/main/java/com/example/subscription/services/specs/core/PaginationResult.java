package com.example.subscription.services.specs.core;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@Data
@Builder
public class PaginationResult<T> {

    long totalItems;

    int currentPage;

    int pageSize;

    List<T> items;
}
