package com.user.usermanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
public class ApplicationUser extends Audit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer autoGenId;
    @NotEmpty(message = "User id can't be empty!")
    private String userId;
    @NotEmpty(message = "User name can't be empty!")
    private String userName;
    @NotEmpty(message = "Password can't be empty!")
    private String password;
    @NotEmpty(message = "Email can't be empty!")
    @Column(unique = true)
    private String email;
    @NotNull
    @Column(unique = true)
    private Long contactNumber;
    @ColumnDefault("false")
    private Boolean isActive;
    private String verificationToken;
    private Long otp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_userId")
    private Set<ApplicationUserAddress> address;
}
