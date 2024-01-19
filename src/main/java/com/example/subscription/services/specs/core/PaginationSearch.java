package com.example.subscription.services.specs.core;

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

    @Max(value = 100, message = "حداکثر 100 ایتم در هر درخواست قابل بازیابی است")
    @Min(value = 10, message = "حداقل سایز هر صفحه 10 است.")
    int pageSize = 10;

    @Min(value = 0, message = "شماره صفحه از 0 شروع می شود")
    int pageNumber = 0;

}
