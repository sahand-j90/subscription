package com.example.subscription.services.validators;

import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RequiredArgsConstructor
public class CreateUserValidation implements Validation {

    private final String username;
    private final UserRepository userRepository;

    @Override
    public void validate() {
        var entity = userRepository.findById(username);

        if (entity.isPresent()) {
            throw new BizException(Errors.USER_ALREADY_EXISTS_EXCEPTION);
        }
    }
}
