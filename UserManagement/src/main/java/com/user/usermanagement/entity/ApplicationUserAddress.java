package com.user.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserAddress extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Line1 can't be empty!")
    private String line1;
    private String line2;
    @NotNull
    private Integer zipCode;
    @NotEmpty(message = "City can't be empty!")
    private String city;
    @NotEmpty(message = "State can't be empty!")
    private String state;
    @NotEmpty(message = "Country can't be empty!")
    private String country;
}
