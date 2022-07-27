package com.user.usermanagement.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommonUtils {

    private final PasswordEncoder passwordEncoder;

    public String generateUserId(String email) {
        return RandomStringUtils.random(20, true, true) + passwordEncoder.encode(email);
    }
}
