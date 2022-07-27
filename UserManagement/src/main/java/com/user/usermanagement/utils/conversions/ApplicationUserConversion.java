package com.user.usermanagement.utils.conversions;

import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.model.ApplicationUserAddressDTO;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationUserConversion {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CommonUtils utils;

    public List<ApplicationUserDTO> convertEntityToDTO(List<ApplicationUser> users) {
        return users.stream().map(user -> modelMapper.map(user, ApplicationUserDTO.class)).collect(Collectors.toList());
    }

    public ApplicationUserDTO convertSingleEntityToDTO(ApplicationUser user) {
        return modelMapper.map(user, ApplicationUserDTO.class);
    }

    public ApplicationUser convertDtoToEntity(ApplicationUserDTO userData) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        ApplicationUserDTO requestUser = Arrays.asList(userData).stream().map(user -> {
            user.setUserId(utils.generateUserId(user.getEmail()));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedDate(timestamp);
            user.setLastModifiedDate((timestamp));
            Set<ApplicationUserAddressDTO> addresses = user.getAddress().stream().map(address -> {
                address.setCreatedDate(timestamp);
                address.setLastModifiedDate(timestamp);
                return address;
            }).collect(Collectors.toSet());
            user.setAddress(addresses);
            return user;
        }).findFirst().get();
        return modelMapper.map(requestUser, ApplicationUser.class);
    }
}
