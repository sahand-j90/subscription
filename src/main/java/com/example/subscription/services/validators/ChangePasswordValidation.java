package com.example.subscription.services.validators;

import com.example.subscription.domains.UserEntity;
import com.example.subscription.exceptions.BizException;
import com.example.subscription.exceptions.Errors;
import com.example.subscription.security.SecurityFacade;
import lombok.RequiredArgsConstructor;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@RequiredArgsConstructor
public class ChangePasswordValidation implements Validation {

    private final UserEntity user;
    private final String rowPassword;
    private final SecurityFacade securityFacade;

    @Override
    public void validate() {

        var isMatched = securityFacade.matchPassword(rowPassword, user.getPassword());

        if (!isMatched) {
            throw new BizException(Errors.INVALID_CREDENTIALS_EXCEPTION);
        }

    }
}
