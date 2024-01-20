package com.example.subscription.services.specs.core;

import com.example.subscription.i18n.Messages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sahand Jalilvand 19.01.24
 */
@Getter
@Setter
public class PaginationSearch {

    @Min(value = 10, message = Messages.PAGE_SIZE_MIN_VALUE)
    @Max(value = 20, message = Messages.PAGE_SIZE_MAX_VALUE)
    int pageSize = 10;

    @Min(value = 0, message = Messages.PAGE_NUMBER_MIN_VALUE)
    int pageNumber = 0;

}
