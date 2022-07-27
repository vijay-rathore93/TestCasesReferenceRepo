package com.user.usermanagement.validations;

import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.utils.UserInputValidator;
import com.user.usermanagement.utils.validations.Validator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.user.usermanagement.utils.ApplicationResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidatorTest {

    @Mock
    private UserInputValidator inputValidator;

    @InjectMocks
    private Validator validator;

    @Test
    @Tag("Invalid email")
    public void isValidInput_invalidEmail() {
        ApplicationUserDTO user = singleUserDTO();
        user.setEmail("abc");
        when(inputValidator.validateEmail(user.getEmail())).thenReturn(false);
        UserException exception = assertThrows(UserException.class, () -> {
            validator.isValidInput(user);
        });
        assertEquals(INVALID_EMAIL, exception.getErrorMessage());

    }

    @Test
    @Tag("Invalid password")
    public void isValidInput_invalidPassword() {
        ApplicationUserDTO user = singleUserDTO();
        user.setPassword("abc");
        when(inputValidator.validateEmail(user.getEmail())).thenReturn(true);
        when(inputValidator.validatePassword(user.getPassword())).thenReturn(false);
        UserException exception = assertThrows(UserException.class, () -> {
            validator.isValidInput(user);
        });
        assertEquals(INVALID_PASSWORD, exception.getErrorMessage());

    }

    @Test
    @Tag("Invalid contact number")
    public void isValidInput_invalidContactNumber() {
        ApplicationUserDTO user = singleUserDTO();
        user.setContactNumber(1234L);
        when(inputValidator.validateEmail(user.getEmail())).thenReturn(true);
        when(inputValidator.validatePassword(user.getPassword())).thenReturn(true);
        UserException exception = assertThrows(UserException.class, () -> {
            validator.isValidInput(user);
        });
        assertEquals(INVALID_CONTACT_NUMBER, exception.getErrorMessage());

    }

    private ApplicationUserDTO singleUserDTO() {
        return ApplicationUserDTO.builder()
            .userName("Email User")
            .password("Pwd9$88")
            .email("abc@gmail.com")
            .contactNumber(7760878789L)
            .build();
    }
}
