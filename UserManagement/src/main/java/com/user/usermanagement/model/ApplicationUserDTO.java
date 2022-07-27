package com.user.usermanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUserDTO {
    private Integer autoGenId;
    @Schema(description = "User ID: auto generated")
    private String userId;
    @Schema(description = "User Name: alpha numeric", required = true)
    private String userName;
    @Schema(description = "Password", required = true)
    private String password;
    @Schema(description = "Email: sample@email.com", required = true)
    private String email;
    @Schema(description = "Contact number: must be 10 digits", required = true)
    private Long contactNumber;
    @Schema(description = "Is user active: auto generated")
    private Boolean isActive;
    @Schema(description = "OTP: auto generated")
    private Long otp;
    @Schema(description = "Token: auto generated to be sent to the user")
    private String verificationToken;
    @Schema(description = "Created date: auto generated")
    private Date createdDate;
    @Schema(description = "Last modified date: auto generated")
    private Date lastModifiedDate;
    @Schema(description = "Address: one ore more addresses", required = true)
    private Set<ApplicationUserAddressDTO> address;
}
