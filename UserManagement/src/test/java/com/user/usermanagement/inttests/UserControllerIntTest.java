package com.user.usermanagement.inttests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.user.usermanagement.UserManagementApplication;
import com.user.usermanagement.entity.ApplicationUser;
import com.user.usermanagement.model.ApplicationUserAddressDTO;
import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.service.SignUpService;
import com.user.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = UserManagementApplication.class
)
@AutoConfigureMockMvc
public class UserControllerIntTest {

    @Autowired
    private UserService userService;
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @LocalServerPort
    private int port;

    private final static String USER_ENDPOINT = "/user";
    private final static String USERS_ENDPOINT = "/users";
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void init() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    @Tag("All users - with data")
    public void getUser() throws JsonProcessingException {
        ApplicationUserDTO newUser = singleUserDTO();
        // Get all
        ResponseEntity<ArrayList> existingUsers = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
        // Create
        restTemplate.postForEntity(createURLWithPort(USER_ENDPOINT), newUser, Void.class);
        // Get all
        ResponseEntity<ArrayList> updatedUsers = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
        List<ApplicationUserDTO> allUsers = convertResponseIntoJsonList(updatedUsers.getBody());
        ApplicationUserDTO createdNewUser = allUsers.get(0);

        assertNotNull(createdNewUser);
        assertNotNull(createdNewUser.getUserId());
        assertFalse(createdNewUser.getIsActive());
        assertNotNull(createdNewUser.getVerificationToken());
        assertEquals(HttpStatus.OK, existingUsers.getStatusCode());
        assertEquals((existingUsers.getBody().size() + 1), updatedUsers.getBody().size());
        assertEquals(newUser.getEmail(), createdNewUser.getEmail());
        assertEquals(newUser.getContactNumber(), createdNewUser.getContactNumber());
    }

    @Test
    @Tag("All users - with data")
    public void getAllUsers() {
        ResponseEntity<ArrayList> existingUsers = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);

        restTemplate.postForEntity(createURLWithPort(USER_ENDPOINT),singleUserDTO(), Void.class);
        ResponseEntity<ArrayList> updatedUsers = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);

        assertNotNull(existingUsers);
        assertEquals(HttpStatus.OK, existingUsers.getStatusCode());
        assertEquals((existingUsers.getBody().size() + 1), updatedUsers.getBody().size());
    }

    @Test
    @Tag("Update a user")
    public void updateUser() throws Exception {
        ApplicationUserDTO singleUserDTO = singleUserDTO();
        // Create
        mockMvc.perform(post(USER_ENDPOINT)
                .content(
                        objectMapper.writeValueAsString(singleUserDTO)
                )
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Get && test
        ResponseEntity<ArrayList> createdNewUser = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
        List<ApplicationUserDTO> applicationUser = convertResponseIntoJsonList(createdNewUser.getBody());
        ApplicationUserDTO user = applicationUser.get(0);
        user.setContactNumber(7767545679L);
        user.setEmail("updatedEmal98989@gmail.com");
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", applicationUser.get(0).getUserId());
        HttpEntity httpEntity = new HttpEntity(user, headers);

        // Update
        restTemplate.postForEntity(createURLWithPort("/updatedUser"), httpEntity, Void.class);

        // Get && test
        ApplicationUserDTO updatedUser = getSingleUser(applicationUser.get(0).getUserId());
        assertEquals(updatedUser.getEmail(), singleUserDTO.getEmail());
        assertEquals(updatedUser.getContactNumber(), singleUserDTO.getContactNumber());
    }

    @Test
    @Tag("Delete a user")
    public void deleteUser() throws JsonProcessingException {
        // Create
        restTemplate.postForEntity(createURLWithPort(USER_ENDPOINT), singleUserDTO(), Void.class);

        // Get && test
        ResponseEntity<ArrayList> createdNewUser = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
        Integer numOfUsers = createdNewUser.getBody().size();

        List<ApplicationUserDTO> applicationUser = convertResponseIntoJsonList(createdNewUser.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", applicationUser.get(0).getUserId());
        HttpEntity httpEntity = new HttpEntity(headers);

        // Delete
        restTemplate.exchange(createURLWithPort(USER_ENDPOINT), HttpMethod.DELETE, httpEntity, Void.class);

        // Get && test
        ResponseEntity<ArrayList> deletedUser = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
        assertEquals((numOfUsers - 1), deletedUser.getBody().size());
    }

    private ApplicationUserDTO getSingleUser(String userId) throws Exception {
        MvcResult result = mockMvc.perform(get(USER_ENDPOINT)
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), ApplicationUserDTO.class);
    }

    private ApplicationUserDTO singleUserDTO() {
        Random rand = new Random();
        Integer randomNum = rand.nextInt(1000000);
        Set<ApplicationUserAddressDTO> address = new HashSet<>(
                Arrays.asList(
                        ApplicationUserAddressDTO.builder()
                                .line1("Line 1")
                                .zipCode(560076)
                                .city("BLR")
                                .state("KA")
                                .country("IN")
                                .build()
                )
        );
        return ApplicationUserDTO.builder()
                .userName("Email User"+randomNum)
                .password("Pwd9$88")
                .email("ym2user"+ randomNum + "@yahoo.co.uk")
                .contactNumber(7760878789L+randomNum)
                .address(address)
                .build();
    }

    private List<ApplicationUserDTO> convertResponseIntoJsonList(ArrayList<Object> response) throws JsonProcessingException {
        List<ApplicationUserDTO> items = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(Object test: response){
            String jsonString = new Gson().toJson(test, Map.class);
            items.add(mapper.readValue(jsonString, new TypeReference<ApplicationUserDTO>(){}));
        }
        return items;
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
