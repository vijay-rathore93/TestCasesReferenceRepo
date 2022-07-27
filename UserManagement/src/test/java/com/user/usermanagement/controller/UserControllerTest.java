package com.user.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

//    @Test
//    @Tag("Get all users: 200 success - way 2")
//    public void getAllUsers2_with200Success() throws Exception {
//
//        List<ApplicationUserDTO> getAllUsers = getAllUsers();
//
//        when(userService.getAllUsers()).thenReturn(getAllUsers);
//        mockMvc.perform(get("/users")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$[0].email", "ym2user@yahoo.co.uk"));
//    }


    @Test
    @Tag("Get all users: 400 bad request")
    public void getUser_with400BadRequest() throws Exception {

        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Get all users: 200 success")
    public void getUser_with200Success() throws Exception {

        ApplicationUserDTO getUser = createRequestPayload();
        String userId = "userId";

        when(userService.getUser(userId)).thenReturn(getUser);
        MvcResult result = mockMvc.perform(get("/user")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String expectedJsonResponseString = objectMapper.writeValueAsString(getUser);
        assertEquals(expectedJsonResponseString, result.getResponse().getContentAsString());
    }

    @Test
    @Tag("Get all users: 200 success")
    public void getAllUsers_with200Success() throws Exception {

        List<ApplicationUserDTO> getAllUsers = getAllUsers();

        when(userService.getAllUsers()).thenReturn(getAllUsers);
        MvcResult result = mockMvc.perform(get("/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String expectedJsonResponseString = objectMapper.writeValueAsString(getAllUsers);
        assertEquals(expectedJsonResponseString, result.getResponse().getContentAsString());
    }

    @Test
    @Tag("Update user: 200 success")
    public void updateUser_with200Success() throws Exception {
        mockMvc.perform(patch("/user")
            .header("userId", "userId")
            .content(
                objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    @Tag("Update user: 400 bad request")
    public void updateUser_with400BadRequest() throws Exception {
        mockMvc.perform(patch("/user")
            .content(
                    objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
    @Test
    @Tag("Update user: 415 ")
    public void updateUser_with415UnsupportedMediaType() throws Exception {
        mockMvc.perform(patch("/user")
            .header("userId", "userId")
            .content(
                    objectMapper.writeValueAsString(createRequestPayload())
            )
            .contentType(MediaType.APPLICATION_ATOM_XML))
            .andDo(print())
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Delete user: 200 success")
    public void deleteUser_with200Success() throws Exception {
        mockMvc.perform(delete("/user")
            .header("userId", "userId")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    @Tag("Delete user: 400 bad request")
    public void deleteUser_with400BadRequest() throws Exception {
        mockMvc.perform(delete("/user")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private List<ApplicationUserDTO> getAllUsers() {
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

    private ApplicationUserDTO createRequestPayload() {
        return ApplicationUserDTO.builder()
            .email("ym2user@yahoo.co.uk")
            .contactNumber(7760878789L)
            .build();
    }
}
