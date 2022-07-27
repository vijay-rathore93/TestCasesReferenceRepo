package com.user.usermanagement.service;

import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.repository.UserRepo;
import com.user.usermanagement.utils.conversions.ApplicationUserConversion;
import com.user.usermanagement.utils.validations.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.user.usermanagement.utils.ApplicationResource.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ApplicationUserConversion converter;
    private final Validator validator;


    public List<ApplicationUserDTO> getAllUsers() {
//        List<ApplicationUser> users = userRepo.findAll();
        List<ApplicationUser> users = userRepo.findAllByOrderByAutoGenIdDesc();
//        List<ApplicationUser> users = userRepo.findAllAuditByOrderByCreatedDateDesc();

        return converter.convertEntityToDTO(users);
    }

    public ApplicationUserDTO getUser(String userId) {
        ApplicationUser user = userRepo.findByUserId(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        return converter.convertSingleEntityToDTO(user);
    }

    public void deleteUser(String userId) {
        ApplicationUser user = userRepo.findByUserId(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        userRepo.delete(user);
    }

    public void updateUser(String userId, ApplicationUserDTO requestUser) {
        validator.isValidInput(requestUser);
        ApplicationUser user = userRepo.findByUserId(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.setContactNumber(requestUser.getContactNumber());
        user.setEmail(requestUser.getEmail());
        userRepo.save(user);
    }
}
