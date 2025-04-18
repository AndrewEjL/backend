package e_was.backend.Controller.User;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import e_was.backend.BackendApplication;
import e_was.backend.DTO.LoginRequest;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
import e_was.backend.Service.User.MyUserService;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyUserService myUserService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private BackendApplication backendApplication;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String DONOR_TABLE = "user_donor";
    private final String RECIPIENT_TABLE = "user_recipient";

    @Test
    public void testGetAll_ShouldReturnUsersList() throws Exception {
        // Arrange
        UserDonor donor1 = createTestDonor(1, "Donor One", "donor1@example.com");
        UserDonor donor2 = createTestDonor(2, "Donor Two", "donor2@example.com");
        List<UserDonor> donors = Arrays.asList(donor1, donor2);
        
        when(myUserService.getAll(DONOR_TABLE)).thenReturn((List) donors);

        // Act & Assert
        mockMvc.perform(get("/api/user/{tableName}/all", DONOR_TABLE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].user_name").value("Donor One"))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].user_name").value("Donor Two"));
    }

    @Test
    public void testGetByID_ExistingUser_ShouldReturnUser() throws Exception {
        // Arrange
        UserDonor donor = createTestDonor(1, "Test Donor", "donor@example.com");
        when(myUserService.getByID(1, DONOR_TABLE)).thenReturn(donor);

        // Act & Assert
        mockMvc.perform(get("/api/user/{tableName}/{id}", DONOR_TABLE, 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.user_name").value("Test Donor"))
               .andExpect(jsonPath("$.email").value("donor@example.com"));
    }

    @Test
    public void testValidatePassword_ValidPassword_ShouldReturnTrue() throws Exception {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("password", "validPassword");
        
        when(myUserService.validatePassword(1, DONOR_TABLE, "validPassword")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/validatePass/{id}", DONOR_TABLE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
    }

    @Test
    public void testValidatePassword_InvalidPassword_ShouldReturnFalse() throws Exception {
        // Arrange
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("password", "invalidPassword");
        
        when(myUserService.validatePassword(1, DONOR_TABLE, "invalidPassword")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/validatePass/{id}", DONOR_TABLE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
    }

    @Test
    public void testAddUser_ValidDonor_ShouldReturnSuccess() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_name", "New Donor");
        requestBody.put("password", "password123");
        requestBody.put("email", "new.donor@example.com");
        requestBody.put("phone_number", "1234567890");
        
        // Mock service behavior - assuming save method doesn't return anything
        doAnswer(invocation -> {
            UserDonor user = invocation.getArgument(0);
            user.setId(1);
            return user;
        }).when(myUserService).save(any(UserDonor.class), eq(DONOR_TABLE));

        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/add", DONOR_TABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value("User registered successfully"));
        
        verify(myUserService).save(any(UserDonor.class), eq(DONOR_TABLE));
    }

    @Test
    public void testAddUser_InvalidTableName_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_name", "New User");
        requestBody.put("password", "password123");
        requestBody.put("email", "new.user@example.com");
        requestBody.put("phone_number", "1234567890");
        
        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/add", "invalid_table")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.message").value("Invalid table name"));
        
        verify(myUserService, never()).save(any(), anyString());
    }

    @Test
    public void testUpdateProfile_ValidData_ShouldReturnSuccess() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_name", "Updated Name");
        requestBody.put("email", "updated@example.com");
        requestBody.put("phone_number", "9876543210");
        
        UserDonor updatedDonor = createTestDonor(1, "Updated Name", "updated@example.com");
        updatedDonor.setPhoneNumber("9876543210");
        
        when(myUserService.updateProfile(any(UserDonor.class), eq(1), eq(DONOR_TABLE)))
            .thenReturn(updatedDonor);

        // Act & Assert
        mockMvc.perform(put("/api/user/{tableName}/update/profile/{id}", DONOR_TABLE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value("User updated successfully"));
        
        verify(myUserService).updateProfile(any(UserDonor.class), eq(1), eq(DONOR_TABLE));
    }

    @Test
    public void testUpdatePassword_ValidData_ShouldReturnSuccess() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("originPassword", "oldPassword");
        requestBody.put("password", "newPassword");
        
        when(myUserService.updatePass("oldPassword", "newPassword", 1, DONOR_TABLE))
            .thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/user/{tableName}/update/password/{id}", DONOR_TABLE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value("Password updated successfully"));
        
        verify(myUserService).updatePass("oldPassword", "newPassword", 1, DONOR_TABLE);
    }

    @Test
    public void testUpdatePassword_IncorrectOriginalPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("originPassword", "wrongPassword");
        requestBody.put("password", "newPassword");
        
        when(myUserService.updatePass("wrongPassword", "newPassword", 1, DONOR_TABLE))
            .thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/user/{tableName}/update/password/{id}", DONOR_TABLE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.success").value(false))
               .andExpect(jsonPath("$.message").value("Incorrect original password"));
    }

    @Test
    public void testDeleteUser_ExistingUser_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(myUserService).delete(1, DONOR_TABLE);

        // Act & Assert
        mockMvc.perform(put("/api/user/{tableName}/delete/{id}", DONOR_TABLE, 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value("User deleted successfully"));
        
        verify(myUserService).delete(1, DONOR_TABLE);
    }

    @Test
    public void testCheckEmail_ExistingEmail_ShouldReturnTrue() throws Exception {
        // Arrange
        when(myUserService.checkEmailExists("existing@example.com")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/user/checkEmail")
                .param("email", "existing@example.com"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    public void testCheckEmail_NonExistingEmail_ShouldReturnFalse() throws Exception {
        // Arrange
        when(myUserService.checkEmailExists("nonexisting@example.com")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/user/checkEmail")
                .param("email", "nonexisting@example.com"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    public void testLogin_ValidCredentials_ShouldReturnUserInfo() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("donor@example.com");
        loginRequest.setPassword("password123");
        
        UserDonor authenticatedUser = createTestDonor(1, "Test Donor", "donor@example.com");
        
        when(myUserService.login(DONOR_TABLE, "donor@example.com", "password123"))
            .thenReturn(authenticatedUser);

        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/login", DONOR_TABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.userType").value("donor"));
    }

    @Test
    public void testLogin_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("donor@example.com");
        loginRequest.setPassword("wrongPassword");
        
        when(myUserService.login(DONOR_TABLE, "donor@example.com", "wrongPassword"))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/user/{tableName}/login", DONOR_TABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetByOrgID_ShouldReturnUsersList() throws Exception {
        // Arrange
        UserRecipient recipient1 = new UserRecipient();
        recipient1.setId(1);
        recipient1.setUserName("Recipient One");
        recipient1.setEmail("recipient1@example.com");
        recipient1.setPassword("hashedPassword");
        recipient1.setPhoneNumber("1111111111");
        recipient1.setUserStatusID(1);
        recipient1.setIsDelete(false);
        recipient1.setIsUpdate(false);
        recipient1.setOrganizationID(1);
        
        UserRecipient recipient2 = new UserRecipient();
        recipient2.setId(2);
        recipient2.setUserName("Recipient Two");
        recipient2.setEmail("recipient2@example.com");
        recipient2.setPassword("hashedPassword");
        recipient2.setPhoneNumber("2222222222");
        recipient2.setUserStatusID(1);
        recipient2.setIsDelete(false);
        recipient2.setIsUpdate(false);
        recipient2.setOrganizationID(1);
        
        List<UserRecipient> recipients = Arrays.asList(recipient1, recipient2);
        
        // Use raw type casting to avoid generic type issues
        when(myUserService.getByOrgID(1, RECIPIENT_TABLE)).thenReturn((List) recipients);

        // Act & Assert
        mockMvc.perform(get("/api/user/{tableName}/org/{id}", RECIPIENT_TABLE, 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].user_name").value("Recipient One"))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].user_name").value("Recipient Two"));
    }

    // Helper method to create test donors
    private UserDonor createTestDonor(int id, String name, String email) {
        UserDonor donor = new UserDonor();
        donor.setId(id);
        donor.setUserName(name);
        donor.setEmail(email);
        donor.setPassword("hashedPassword");
        donor.setPhoneNumber("1234567890");
        donor.setUserStatusID(1);
        donor.setIsDelete(false);
        donor.setIsUpdate(false);
        donor.setRewardPoints(100);
        return donor;
    }
} 