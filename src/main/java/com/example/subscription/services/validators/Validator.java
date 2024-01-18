package com.example.subscription.services.validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sahand Jalilvand 18.01.24
 */
public final class Validator {

    private final List<Validation> validations = new ArrayList<>();

    public Validator with(Validation validation) {
        validations.add(validation);
        return this;
    }

    public void validate() {
        validations.forEach(Validation::validate);
    }
}
