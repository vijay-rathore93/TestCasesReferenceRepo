package com.user.usermanagement.service;

import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.repository.UserRepo;
import com.user.usermanagement.utils.conversions.ApplicationUserConversion;
import com.user.usermanagement.utils.validations.Validator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.user.usermanagement.utils.ApplicationResource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private ApplicationUserConversion applicationUserConversion;
    @Mock
    private Validator validator;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private EmailSenderService emailSenderService;
    @InjectMocks
    private SignUpService signUpService;

    @Test
    @Tag("Create user - 201")
    public void createUser_with201() {
        ApplicationUserDTO userDTO = singleUserDTO();
        ApplicationUser userEntity = singleUserEntity();
        doNothing().when(validator).isValidInput(userDTO);
        doNothing().when(emailSenderService).sendEmail(userEntity, null);
        when(applicationUserConversion.convertDtoToEntity(userDTO)).thenReturn(userEntity);
        signUpService.createUser(userDTO, null);
        Mockito.verify(userRepo).save(userEntity);
        Mockito.verify(userRepo, times(1)).save(userEntity);
    }




    @Test
    @Tag("Create user - 201")
    public void resetPassword_withSuccess() {
        String userId = "userId";
        ApplicationUserDTO userDTO = singleUserDTO();
        ApplicationUser userEntity = singleUserEntity();
        doNothing().when(validator).isValidInput(userDTO);
        when(userRepo.findByUserId(userId)).thenReturn(Optional.of(userEntity));
        signUpService.resetPassword(userId, userDTO);
        Mockito.verify(userRepo).save(userEntity);
        Mockito.verify(userRepo, times(1)).save(userEntity);
    }

    @Test
    @Tag("Create user - 201")
    public void resetPassword_withError() {
        String userId = "userId";
        ApplicationUserDTO userDTO = singleUserDTO();
        doNothing().when(validator).isValidInput(userDTO);
        when(userRepo.findByUserId(userId)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> {
            signUpService.resetPassword(userId, userDTO);
        });
    }

    @Test
    @Tag("Create user - 201")
    public void verifyUser_withSuccess() {
        String token = "token";
        ApplicationUser userEntity = singleUserEntity();
        when(userRepo.findByVerificationToken(token)).thenReturn(Optional.of(userEntity));
        when(userRepo.save(userEntity)).thenReturn(userEntity);
        String result = signUpService.verifyUser(token);
        assertNotNull(result);
        assertEquals(VERIFICATION_LINK_SUCCESS, result);
    }

    @Test
    @Tag("Create user - error")
    public void verifyUser_withError() {
        String token = "token";
        when(userRepo.findByVerificationToken(token)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> {
            signUpService.verifyUser(token);
        });
    }

    @Test
    @Tag("Create user - error")
    public void verifyUser_withError2() {
        String token = "token";
        ApplicationUser userEntity = singleUserEntity();
        when(userRepo.findByVerificationToken(token)).thenReturn(Optional.of(userEntity));
        when(userRepo.save(userEntity)).thenReturn(null);
        assertThrows(UserException.class, () -> {
            signUpService.verifyUser(token);
        });
    }

    @Test
    @Tag("Send OTP: 200")
    public void sendOTP_withSuccess() {
        ApplicationUser userEntity = singleUserEntity();
        String email = "ym2user@yahoo.co.uk";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userRepo.save(userEntity)).thenReturn(userEntity);
        String response = signUpService.sendOtp(email);
        assertNotNull(response);
        assertEquals(OTP_SEND_SUCCESS, response);
    }

    @Test
    @Tag("Send OTP: Error - no email")
    public void sendOTP_withNoEmail() {
        String email = "ym2user@yahoo.co.uk";
        doThrow(new UserException(USER_NOT_FOUND)).when(userRepo).findByEmail(email);
        UserException exception = assertThrows(UserException.class, () -> {
            signUpService.sendOtp(email);
        });
        assertEquals(USER_NOT_FOUND, exception.getErrorMessage());
    }

    @Test
    @Tag("Send OTP: Error - user inactive")
    public void sendOTP_withInactiveUser() {
        String email = "ym2user@yahoo.co.uk";
        ApplicationUser user = singleUserEntity();
        user.setIsActive(false);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        UserException exception = assertThrows(UserException.class, () -> {
            signUpService.sendOtp(email);
        });
        assertEquals(INACTIVE_USER, exception.getErrorMessage());
    }

    @Test
    @Tag("Send OTP: null user entity")
    public void sendOTP_withNullUserEntity() {
        String email = "ym2user@yahoo.co.uk";
        ApplicationUser user = singleUserEntity();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(null);
        UserException exception = assertThrows(UserException.class, () -> {
            signUpService.sendOtp(email);
        });
        assertEquals(OTP_SEND_ERROR, exception.getErrorMessage());
    }

    private ApplicationUserDTO singleUserDTO() {
        return ApplicationUserDTO.builder()
            .userName("Email User")
            .password("Pwd9$88")
            .email("ym2user@yahoo.co.uk")
            .contactNumber(7760878789L)
            .build();
    }

    private ApplicationUser singleUserEntity() {
        return ApplicationUser.builder()
            .userName("Email User")
            .password("Pwd9$88")
            .email("ym2user@yahoo.co.uk")
            .contactNumber(7760878789L)
            .isActive(true)
            .build();
    }
}
