package com.user.usermanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationUserAddressDTO {
    @Schema(description = "Address line 1", required = true)
    private String line1;
    @Schema(description = "Address line 2")
    private String line2;
    @Schema(description = "Zip code", required = true)
    private Integer zipCode;
    @Schema(description = "City", required = true)
    private String city;
    @Schema(description = "State", required = true)
    private String state;
    @Schema(description = "Country", required = true)
    private String country;
    @Schema(description = "Created date")
    private Date createdDate;
    @Schema(description = "Last modified date")
    private Date lastModifiedDate;
}
