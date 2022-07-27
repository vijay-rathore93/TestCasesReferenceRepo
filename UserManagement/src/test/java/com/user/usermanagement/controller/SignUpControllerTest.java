package com.user.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.usermanagement.model.ApplicationUserAddressDTO;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.service.SignUpService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = SignUpController.class)
public class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SignUpService signUpService;

    @Test
    @Tag("Verification link: test 200 success")
    public void link_with200Success() throws Exception {
        mockMvc.perform(post("/link")
            .content(
                    objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Tag("Verification link: test 400 bad request")
    public void link_with400BadRequest() throws Exception {
        mockMvc.perform(post("/link")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Verification link: test 415 unsupported media type")
    public void link_with415UnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/link")
                .content(
                        objectMapper.writeValueAsString(createRequestPayload())
                )
                .contentType(MediaType.APPLICATION_ATOM_XML))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Send OTP: test 200 success")
    public void sendOtp_with200Success() throws Exception {
        mockMvc.perform(post("/otp")
            .content(
                objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Tag("Send OTP: test 400 bad request")
    public void sendOtp_with400BadRequest() throws Exception {
        mockMvc.perform(post("/otp")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Send OTP: test 415 unsupported media type")
    public void sendOtp_with415UnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/otp")
            .content(
                objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_ATOM_XML))
            .andDo(print())
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Verify OTP: test 200 success")
    public void verifyOtp_with200Success() throws Exception {
        mockMvc.perform(post("/otpValidator")
            .content(
                objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Tag("Verify OTP: test 400 bad request")
    public void verifyOtp_with400BadRequest() throws Exception {
        mockMvc.perform(post("/otpValidator")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Verify OTP: test 415 unsupported media type")
    public void verifyOtp_with415UnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/otpValidator")
                .content(
                        objectMapper.writeValueAsString(createRequestPayload())
                )
                .contentType(MediaType.APPLICATION_ATOM_XML))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Validate token: test 200 success")
    public void validateToken_with200Success() throws Exception {
        mockMvc.perform(get("/tokenValidator")
                .param("token", "abcdef")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("Validate token: test 400 bad request")
    public void validateToken_with400BadRequest() throws Exception {
        mockMvc.perform(get("/tokenValidator")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Reset password: test 200 success")
    public void resetPassword_with200Success() throws Exception {
        mockMvc.perform(patch("/password")
                .header("userId", "abcdef")
                .content(
                objectMapper.writeValueAsString(createRequestPayload())
        ).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("Reset password: test 400 bad request - no request body")
    public void resetPassword_with400BadRequestAndNoRequestBody() throws Exception {
        mockMvc.perform(patch("/password")
                .header("userId", "abcdef")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Reset password: test 400 bad request - no request header")
    public void resetPassword_with400BadRequestAndNoRequestHeader() throws Exception {
        mockMvc.perform(patch("/password")
                .content(
                        objectMapper.writeValueAsString(createRequestPayload())
                ).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Reset password: test 415 unsupported media type")
    public void resetPassword_with200UnsupportedMediaType() throws Exception {
        mockMvc.perform(patch("/password")
                .header("userId", "abcdef")
                .content(
                        objectMapper.writeValueAsString(createRequestPayload())
                ).contentType(MediaType.APPLICATION_ATOM_XML))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Create user: test 201 success")
    public void createUser_with201Success() throws Exception {
        mockMvc.perform(post("/user").content(
            objectMapper.writeValueAsString(createRequestPayload())
        ).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    @Tag("Create user: test 400 bad request")
    public void createUser_with400BadRequest() throws Exception {
        mockMvc.perform(post("/user")
        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Create user: test 415 unsupported media type")
    public void createUser_with415UnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_ATOM_XML))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    private ApplicationUserDTO createRequestPayload() {
        Set<ApplicationUserAddressDTO> address = new HashSet<>(
                Arrays.asList(
                        ApplicationUserAddressDTO.builder()
                        .line1("Line 1")
                        .zipCode(560076)
                        .city("BLR")
                        .state("KA")
                        .country("IN")
                        .build()
                )
        );
        return ApplicationUserDTO.builder()
                .userName("Email User")
                .password("Pwd9$88")
                .email("ym2user@yahoo.co.uk")
                .contactNumber(7760878789L)
                .address(address)
                .build();
    }
}
