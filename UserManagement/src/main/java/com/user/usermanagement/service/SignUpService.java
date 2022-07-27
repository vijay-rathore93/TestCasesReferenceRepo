package com.user.usermanagement.service;

import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.repository.UserRepo;
import com.user.usermanagement.utils.conversions.ApplicationUserConversion;
import com.user.usermanagement.utils.validations.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;

import static com.user.usermanagement.utils.ApplicationResource.*;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepo userRepo;
    private final ApplicationUserConversion applicationUserConversion;
    private final Validator validator;
    private final PasswordEncoder encoder;
    private final EmailSenderService emailSenderService;

    public void createUser(ApplicationUserDTO requestUser, HttpServletRequest servletRequest) {
        validator.isValidInput(requestUser);
        ApplicationUser applicationUser = applicationUserConversion.convertDtoToEntity(requestUser);
        applicationUser.setVerificationToken(UUID.randomUUID().toString());
        userRepo.save(applicationUser);
        emailSenderService.sendEmail(applicationUser, servletRequest);
    }

    public void resetPassword(String userId, ApplicationUserDTO requestUser) {
        validator.isValidInput(requestUser);
        ApplicationUser user = userRepo.findByUserId(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.setPassword(encoder.encode(requestUser.getPassword()));
        userRepo.save(user);
    }

    public String verifyUser(String token) {
        ApplicationUser user = userRepo.findByVerificationToken(token).orElseThrow(() -> new UserException(INVALID_TOKEN));
        user.setIsActive(true);
        user.setVerificationToken(null);
        ApplicationUser updatedUser = userRepo.save(user);
        if(updatedUser == null) {
            throw new UserException(VERIFICATION_LINK_ERROR);
        }
        return VERIFICATION_LINK_SUCCESS;
    }

    // public String sendOtp(ApplicationUserDTO requestUser) {
    public String sendOtp(String email) {
        ApplicationUser user = userRepo.findByEmail(email).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        if (!user.getIsActive()) {
            throw new UserException(INACTIVE_USER);
        }
        Random rand = new Random();
        user.setOtp(rand.nextInt(10) + rand.nextLong());
        ApplicationUser updatedUser = userRepo.save(user);
        if (updatedUser == null) {
            throw new UserException(OTP_SEND_ERROR);
        }
        emailSenderService.sendOTP(user);
        return OTP_SEND_SUCCESS;
    }

    // public String verifyOtp(ApplicationUserDTO requestUser) {
    public String verifyOtp(Long otp) {
        ApplicationUser user = userRepo.findByOtp(otp).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.setOtp(null);
        ApplicationUser updatedUser = userRepo.save(user);
        if (updatedUser == null) {
            throw new UserException(OTP_VERIFICATION_ERROR);
        }
        return OTP_VERIFICATION_SUCCESS + RESET_PASSWORD;
    }

    public String sendVerificationLink(String email, HttpServletRequest servletRequest) {
        ApplicationUser user = userRepo.findByEmail(email).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setIsActive(false);
        userRepo.save(user);
        emailSenderService.sendEmail(user, servletRequest);
        return VERIFICATION_LINK_SENT;
    }
}
