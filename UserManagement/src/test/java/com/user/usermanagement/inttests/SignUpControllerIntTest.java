package com.user.usermanagement.inttests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.user.usermanagement.UserManagementApplication;
import com.user.usermanagement.exception.UserException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.user.usermanagement.utils.ApplicationResource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = UserManagementApplication.class
)
@AutoConfigureMockMvc
public class SignUpControllerIntTest {

    @Autowired
    private UserService userService;
    @Autowired
    private SignUpService signUpService;
    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final static String USER_ENDPOINT = "/user";
    private final static String USERS_ENDPOINT = "/users";
    private TestRestTemplate testTemplate;
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        testTemplate = new TestRestTemplate();
        restTemplate = new RestTemplate();
    }

    @Test
    @Tag("Resend verification link - success")
    public void resendVerificationLink() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(post("/link")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(VERIFICATION_LINK_SENT));

        ApplicationUserDTO user2 = getSingleUser(user.getUserId());
        assertFalse(user2.getIsActive());
        assertNotNull(user2.getVerificationToken());
    }

    @Test
    @Tag("Resend verification link - no input")
    public void resendVerificationLink_noInput() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(post("/link")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Resend verification link - incorrect media type")
    public void resendVerificationLink_incorrectMediaType() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(post("/link")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Resend verification link - incorrect method")
    public void resendVerificationLink_incorrectMethod() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(get("/link")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Tag("OTP Validator")
    public void verifyOtp() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(get("/tokenValidator")
                .param("token", user.getVerificationToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/otp")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        ApplicationUserDTO userById = getSingleUser(user.getUserId());
        Long otp = userById.getOtp();
        mockMvc.perform(post("/otpValidator")
                .content(
                        String.valueOf(otp)
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(OTP_VERIFICATION_SUCCESS + RESET_PASSWORD));

        ApplicationUserDTO userByIdNullOTP = getSingleUser(user.getUserId());
        assertNull(userByIdNullOTP.getOtp());
    }

    @Test
    @Tag("OTP Validator - incorrect method")
    public void verifyOtp_incorrectMethod() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(get("/tokenValidator")
                .param("token", user.getVerificationToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/otp")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        ApplicationUserDTO userById = getSingleUser(user.getUserId());
        Long otp = userById.getOtp();
        mockMvc.perform(get("/otpValidator")
                .content(
                        String.valueOf(otp)
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Tag("OTP Validator - no input")
    public void verifyOtp_noInput() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(get("/tokenValidator")
                .param("token", user.getVerificationToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/otp")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        ApplicationUserDTO userById = getSingleUser(user.getUserId());
        Long otp = userById.getOtp();
        mockMvc.perform(post("/otpValidator")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("OTP Validator - incorrect media type")
    public void verifyOtp_incorrectMediaType() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);

        mockMvc.perform(get("/tokenValidator")
                .param("token", user.getVerificationToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/otp")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        ApplicationUserDTO userById = getSingleUser(user.getUserId());
        Long otp = userById.getOtp();
        mockMvc.perform(post("/otpValidator")
                .content(
                        String.valueOf(otp)
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Send OTP")
    public void sendOtp() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);
        String token = user.getVerificationToken();

        mockMvc.perform(get("/tokenValidator")
                .param("token", token)
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(VERIFICATION_LINK_SUCCESS));

        MvcResult result = mockMvc.perform(post("/otp")
                .content(
                    user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse());
        assertEquals(OTP_SEND_SUCCESS, result.getResponse().getContentAsString());
    }

    @Test
    @Tag("Send OTP - inactive user")
    public void sendOtp_inactiveUser() throws Exception {
        ApplicationUserDTO user = createAndGetUserFunc().get(0);
        String token = user.getVerificationToken();

        mockMvc.perform(post("/otp")
                .content(
                        user.getEmail()
                )
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserException))
                .andReturn();
        // TODO ^^
        // Assert the exception message
    }

    @Test
    @Tag("Send OTP - no input email")
    public void sendOtp_noEmail() throws Exception {

        mockMvc.perform(post("/otp")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Send OTP - incorrect media type")
    public void sendOtp_incorrectMediaType() throws Exception {

        mockMvc.perform(post("/otp")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("Send OTP - invalid method")
    public void sendOtp_methodNotAllowed() throws Exception {

        mockMvc.perform(get("/otp")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Tag("Reset password")
    public void resetPassword() throws Exception {
        List<ApplicationUserDTO> users = createAndGetUserFunc();
        String userId = users.get(0).getUserId();
        ApplicationUserDTO user = singleUserDTO();
        user.setPassword("Pwd9$100");
        mockMvc.perform(patch("/password")
                .header("userId", userId)
                .content(
                    objectMapper.writeValueAsString(user)
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

//        ApplicationUserDTO userFromDB = getSingleUser(userId);
//        assertNotNull(userFromDB);
//        assertEquals(passwordEncoder.encode(user.getPassword()), userFromDB.getPassword());
    }

    @Test
    @Tag("Reset password - bad request")
    public void resetPassword_with400() throws Exception {
        mockMvc.perform(patch("/password")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Reset password - incorrect password pattern")
    public void resetPassword_withWrongPattern() throws Exception {
        List<ApplicationUserDTO> users = createAndGetUserFunc();
        String userId = users.get(0).getUserId();
        ApplicationUserDTO user = singleUserDTO();
        user.setPassword("12345");

        MvcResult mvcResult = mockMvc.perform(patch("/password")
                .header("userId", userId)
                .content(
                    objectMapper.writeValueAsString(user)
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserException))
                .andReturn();

//        ApiResponse<String> apiResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiResponse.class);
//        assertEquals(INVALID_PASSWORD, apiResponse.getMessage());
    }

    @Test
    @Tag("Validate token")
    public void validateToken() throws Exception {
        List<ApplicationUserDTO> users = createAndGetUserFunc();
        String userId = users.get(0).getUserId();

        ApplicationUserDTO singleUser = getSingleUser(userId);
        assertNotNull(singleUser);
        assertFalse(singleUser.getIsActive());

        mockMvc.perform(get("/tokenValidator")
                .param("token", singleUser.getVerificationToken())
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(VERIFICATION_LINK_SUCCESS));

        ApplicationUserDTO singleUserUpdated = getSingleUser(userId);
        assertNotNull(singleUserUpdated);
        assertTrue(singleUserUpdated.getIsActive());
        assertNull(singleUserUpdated.getVerificationToken());
    }

    @Test
    @Tag("Validate token - bad request")
    public void validateToken_with400() throws Exception {
        mockMvc.perform(get("/tokenValidator")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("Validate token - method not allowed")
    public void validateToken_with405() throws Exception {
        mockMvc.perform(post("/tokenValidator")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Tag("Validate token - media type not allowed")
    public void validateToken_with415() throws Exception {
        List<ApplicationUserDTO> users = createAndGetUserFunc();
        String token = users.get(0).getVerificationToken();

        mockMvc.perform(get("/tokenValidator")
                .param("token", token))
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @Tag("All users - no data")
    public void createUser() throws Exception {
        mockMvc.perform(post(USER_ENDPOINT)
                .content(
                    objectMapper.writeValueAsString(singleUserDTO())
                )
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

//    @Test
//    @Tag("Delete a user")
//    public void deleteUser() throws JsonProcessingException {
//        // Create
//        restTemplate.postForEntity(createURLWithPort(USER_ENDPOINT), singleUserDTO(), Void.class);
//
//        // Get && test
//        ResponseEntity<ArrayList> createdNewUser = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
//        Integer numOfUsers = createdNewUser.getBody().size();
//
//        List<ApplicationUserDTO> applicationUser = convertResponseIntoJsonList(createdNewUser.getBody());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("userId", applicationUser.get(0).getUserId());
//        HttpEntity httpEntity = new HttpEntity(headers);
//
//        // Delete
//        restTemplate.exchange(createURLWithPort(USER_ENDPOINT), HttpMethod.DELETE, httpEntity, Void.class);
//
//        // Get && test
//        ResponseEntity<ArrayList> deletedUser = restTemplate.getForEntity(createURLWithPort(USERS_ENDPOINT), ArrayList.class);
//        assertEquals((numOfUsers - 1), deletedUser.getBody().size());
//    }

    private ApplicationUserDTO getSingleUser(String userId) throws Exception {
        MvcResult result = mockMvc.perform(get(USER_ENDPOINT)
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), ApplicationUserDTO.class);
    }

    private List<ApplicationUserDTO> createAndGetUserFunc() throws Exception {
        mockMvc.perform(post(USER_ENDPOINT)
                .content(
                        objectMapper.writeValueAsString(singleUserDTO())
                )
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ArrayList<Object> returnedResult = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        return convertResponseIntoJsonList(returnedResult);
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
