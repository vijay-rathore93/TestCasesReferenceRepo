package com.user.usermanagement.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.user.usermanagement.entity.ApplicationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @InjectMocks
    EmailSenderService emailSenderService;

    private GreenMail greenMail;
    private JavaMailSender javaMailSender;

    @BeforeEach()
    void init() {
        greenMail = new GreenMail(ServerSetup.SMTP);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(3025);
        mailSender.setUsername("SenderEmail");
        mailSender.setPassword("SenderEmailPassword");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        javaMailSender = mailSender;

        greenMail.setUser("testmail@mail.com", "SenderEmail", "SenderEmailPassword");
        ReflectionTestUtils.setField(emailSenderService, "javaMailSender", javaMailSender);
    }

    @Test
    @Tag("")
    public void sendEmail_withSuccess() throws MessagingException {
        greenMail.start();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "localhost");
        request.setLocalPort(8081);

        emailSenderService.sendEmail(applicationUser(), request);

        Message[] message = greenMail.getReceivedMessages();
        assertEquals("Registration Email Verification", message[0].getSubject());
//        assertEquals("Activation Link Sent", emailService.sendMail(user));

        greenMail.stop();
    }

    private ApplicationUser applicationUser() {
        Random rand = new Random();
        Integer randomNum = rand.nextInt(1000000);
        return ApplicationUser.builder()
                .userName("Email User"+randomNum)
                .password("Pwd9$88")
                .email("ym2user"+ randomNum + "@yahoo.co.uk")
                .verificationToken("abc123678859005")
                .contactNumber(7760878789L+randomNum)
                .build();
    }
}
