package com.user.usermanagement.controller;

import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "(A) User Registration Manager", description = "User sign up manager")
public class SignUpController {

    private final SignUpService userService;
    @Operation(
        summary = "Create user",
        description = "To create a single user"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported media type (json required)"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @PostMapping(value ="/user")
    public ResponseEntity<Void> create(@RequestBody @Valid ApplicationUserDTO user, HttpServletRequest servletRequest) {
        userService.createUser(user, servletRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Reset password",
            description = "Reset password"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resetPassword(@RequestHeader String userId, @RequestBody @Valid ApplicationUserDTO user) {
        userService.resetPassword(userId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Validate token",
            description = "Validate token"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @GetMapping(value ="/tokenValidator", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        return new ResponseEntity<>(userService.verifyUser(token), HttpStatus.OK);
    }

    @Operation(
            summary = "Send OTP",
            description = "Send OTP"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @PostMapping(value ="/otp", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sendOtp(@RequestBody @Valid String email) {
        return new ResponseEntity<>(userService.sendOtp(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Validate token",
            description = "Validate token"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported media type (json required)"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @PostMapping(value = "/otpValidator", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> verifyOtp(@RequestBody @Valid String otp) {
        return new ResponseEntity<>(userService.verifyOtp(Long.valueOf(otp)), HttpStatus.OK);
    }

    @Operation(
            summary = "Send verification link",
            description = "Send verification link"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported media type (json required)"
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed"
                    )
            }
    )
    @PostMapping(value ="/link", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> link(@RequestBody @Valid String email, HttpServletRequest servletRequest) {
        return new ResponseEntity<>(userService.sendVerificationLink(email, servletRequest), HttpStatus.OK);
    }
}
