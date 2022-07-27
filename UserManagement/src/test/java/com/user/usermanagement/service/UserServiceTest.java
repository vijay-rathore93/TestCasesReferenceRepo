package com.user.usermanagement.service;

import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.exception.UserException;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.repository.UserRepo;
import com.user.usermanagement.utils.conversions.ApplicationUserConversion;
import com.user.usermanagement.utils.validations.Validator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private ApplicationUserConversion converter;
    @Mock
    private Validator validator;
    @InjectMocks
    private UserService userService;

    @Test
    @Tag("Get all users")
    public void getAllUsers_200Success() {
        List<ApplicationUser> allUsers = getAllUsers();
        when(userRepo.findAll()).thenReturn(allUsers);
        when(converter.convertEntityToDTO(allUsers)).thenReturn(getAllUsersDTO());
        List<ApplicationUserDTO> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Email User", users.get(0).getUserName());
    }

    @Test
    @Tag("Get a single user")
    public void getUser_withNoUserFound() {
        String userId = "userIdValue";
        when(userRepo.findByUserId(userId)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> {
            userService.getUser(userId);
        });
    }

    @Test
    @Tag("Delete a user")
    public void deleteUser_withNoUserFound() {
        String userId = "userIdValue";
        when(userRepo.findByUserId(userId)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> {
            userService.deleteUser(userId);
        });
    }

    @Test
    @Tag("Delete a user")
    public void deleteUser_withSuccess() {
        String userId = "userIdValue";
        ApplicationUser user = getAllUsers().get(0);
        when(userRepo.findByUserId(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        verify(userRepo).delete(user);
        verify(userRepo, times(1)).delete(user);
    }

    @Test
    @Tag("Update a user")
    public void updateUser_withSuccess() {
        String userId = "userIdValue";
        ApplicationUserDTO user = getAllUsersDTO().get(0);
        ApplicationUser userEntity = getAllUsers().get(0);

        doNothing().when(validator).isValidInput(user);
        when(userRepo.findByUserId(userId)).thenReturn(Optional.of(userEntity));
        userService.updateUser(userId, user);
        verify(userRepo).save(userEntity);
        verify(userRepo, times(1)).save(userEntity);
    }

    @Test
    @Tag("Update a user")
    public void updateUser_withNoUserFound() {
        doNothing().when(validator).isValidInput(getAllUsersDTO().get(0));
        String userId = "userIdValue";
        when(userRepo.findByUserId(userId)).thenThrow(UserException.class);
        assertThrows(UserException.class, () -> {
            userService.updateUser(userId, getAllUsersDTO().get(0));
        });
    }

    @Test
    @Tag("Get a single user")
    public void getUser_200Success() {
        ApplicationUser user = getAllUsers().get(0);
        ApplicationUserDTO userDTO = getAllUsersDTO().get(0);
        String userId = "userIdValue";
        when(userRepo.findByUserId(userId)).thenReturn(Optional.of(user));
        when(converter.convertSingleEntityToDTO(user)).thenReturn(userDTO);
        ApplicationUserDTO singleUser = userService.getUser(userId);
        assertNotNull(singleUser);
        assertEquals(userDTO.getUserName(), singleUser.getUserName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    private List<ApplicationUserDTO> getAllUsersDTO() {
        return new ArrayList<ApplicationUserDTO>(
            Arrays.asList(
                ApplicationUserDTO.builder()
                    .userName("Email User")
                    .password("Pwd9$88")
                    .email("ym2user@yahoo.co.uk")
                    .contactNumber(7760878789L)
                    .build(),
                ApplicationUserDTO.builder()
                    .userName("Another User")
                    .password("Pwd9$88")
                    .email("someotehruser@yahoo.co.uk")
                    .contactNumber(7760765432L)
                    .build()
            )
        );
    }

    private List<ApplicationUser> getAllUsers() {
        return new ArrayList<ApplicationUser>(
            Arrays.asList(
                ApplicationUser.builder()
                    .userName("Email User")
                    .password("Pwd9$88")
                    .email("ym2user@yahoo.co.uk")
                    .contactNumber(7760878789L)
                    .build(),
                ApplicationUser.builder()
                    .userName("Another User")
                    .password("Pwd9$88")
                    .email("someotehruser@yahoo.co.uk")
                    .contactNumber(7760765432L)
                    .build()
            )
        );
    }
}
