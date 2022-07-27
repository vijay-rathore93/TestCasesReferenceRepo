package com.user.usermanagement.utils.validations;

import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.utils.UserInputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.user.usermanagement.utils.ApplicationResource.*;

@Component
@RequiredArgsConstructor
public class Validator {
    private final UserInputValidator inputValidator;

    private Boolean isValidEmail(String email) {
        return inputValidator.validateEmail(email);
    }

    private Boolean isValidPassword(String password) {
        return inputValidator.validatePassword(password);
    }

    public void isValidInput(ApplicationUserDTO user) {
        if (user.getEmail() != null && !isValidEmail(user.getEmail())) {
             throw new UserException(INVALID_EMAIL);
        }
        if (user.getPassword() != null && !isValidPassword(user.getPassword())) {
            throw new UserException(INVALID_PASSWORD);
        }
        if (user.getContactNumber() != null && user.getContactNumber().toString().length() != 10) {
            throw new UserException(INVALID_CONTACT_NUMBER);
        }
    }
}
