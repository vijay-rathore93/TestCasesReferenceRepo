package com.user.usermanagement.repository;

import com.user.usermanagement.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findByUserId(String userId);

    Optional<ApplicationUser> findByVerificationToken(String token);

    Optional<ApplicationUser> findByEmail(String email);

    Optional<ApplicationUser> findByOtp(Long otp);

    List<ApplicationUser> findAllByOrderByAutoGenIdDesc();

//    List<ApplicationUser> findAllOrderByCreatedDateDesc();
}
