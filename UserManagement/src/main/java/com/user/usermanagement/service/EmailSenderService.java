package com.user.usermanagement.service;

import com.user.usermanagement.entity.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.text.MessageFormat;

import static com.user.usermanagement.utils.ApplicationResource.*;

@Component
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public void sendEmail(ApplicationUser applicationUser, HttpServletRequest servletRequest) {

        String url = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(applicationUser.getEmail());
        registrationEmail.setSubject(EMAIL_REGISTRATION_SUBJECT);
        registrationEmail.setText(MessageFormat.format(EMAIL_REGISTRATION_MESSAGE, url, applicationUser.getVerificationToken()));
        mailSender.send(registrationEmail);
    }

    public void sendOTP(ApplicationUser applicationUser) {
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(applicationUser.getEmail());
        registrationEmail.setSubject(EMAIL_OTP_SUBJECT);
        registrationEmail.setText(MessageFormat.format(EMAIL_OTP_MESSAGE, applicationUser.getUserName(), applicationUser.getOtp()));
        mailSender.send(registrationEmail);
    }
}
