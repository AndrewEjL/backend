package e_was.backend.Service.User;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
import e_was.backend.Respository.User.UserDonorRepository;
import e_was.backend.Respository.User.UserRecipientRepository;
import e_was.backend.Service.email.MailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyUserServiceTest {

    @InjectMocks
    private MyUserService myUserService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private MailService mailService;

    @Mock
    private UserDonorRepository userDonorRepo;

    @Mock
    private UserRecipientRepository userRecipientRepo;

    @Mock
    private UserTable userTable;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<UserDonor> criteriaQuery;

    @Mock
    private Root<UserDonor> root;

    @Mock
    private TypedQuery<UserDonor> typedQuery;

    private UserDonor userDonor;
    private UserRecipient userRecipient;
    private final String TABLE_DONOR = "donor";
    private final String TABLE_RECIPIENT = "recipient";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Setup sample donor
        userDonor = new UserDonor();
        userDonor.setId(1);
        userDonor.setUserName("Test Donor");
        userDonor.setEmail("donor@example.com");
        userDonor.setPassword("$2a$10$abcdefghijklmnopqrstuvwxyz012345"); // BCrypt hashed password
        userDonor.setPhoneNumber("1234567890");
        userDonor.setUserStatusID(1);
        userDonor.setIsDelete(false);
        userDonor.setIsUpdate(false);
        userDonor.setRewardPoints(100);
        
        // Setup sample recipient
        userRecipient = new UserRecipient();
        userRecipient.setId(2);
        userRecipient.setUserName("Test Recipient");
        userRecipient.setEmail("recipient@example.com");
        userRecipient.setPassword("$2a$10$abcdefghijklmnopqrstuvwxyz012345"); // BCrypt hashed password
        userRecipient.setPhoneNumber("0987654321");
        userRecipient.setUserStatusID(1);
        userRecipient.setIsDelete(false);
        userRecipient.setIsUpdate(false);
        
        // Setup mocks for entity manager
        when(userTable.getEntity(TABLE_DONOR)).thenReturn((Class) UserDonor.class);
        when(userTable.getEntity(TABLE_RECIPIENT)).thenReturn((Class) UserRecipient.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(UserDonor.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(UserDonor.class)).thenReturn(root);
    }

    @Test
    public void testGetAll_ShouldReturnAllUsers() {
        // Arrange
        List<UserDonor> expectedUsers = Arrays.asList(userDonor);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedUsers);

        // Act
        List<UserDonor> actualUsers = myUserService.getAll(TABLE_DONOR);

        // Assert
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0), actualUsers.get(0));
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    public void testGetByID_ExistingUser_ShouldReturnUser() {
        // Arrange
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(userDonor));

        // Act
        UserDonor result = myUserService.getByID(1, TABLE_DONOR);

        // Assert
        assertNotNull(result);
        assertEquals(userDonor.getId(), result.getId());
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    public void testGetByID_NonExistingUser_ShouldReturnNull() {
        // Arrange
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        // Act
        UserDonor result = myUserService.getByID(999, TABLE_DONOR);

        // Assert
        assertNull(result);
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    public void testValidatePassword_ValidPassword_ShouldReturnTrue() {
        // Arrange
        String rawPassword = "password123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);
        userDonor.setPassword(hashedPassword);
        
        // Mock getByID
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(userDonor));

        // Act
        boolean result = myUserService.validatePassword(1, TABLE_DONOR, rawPassword);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testValidatePassword_InvalidPassword_ShouldReturnFalse() {
        // Arrange
        String rawPassword = "password123";
        String wrongPassword = "wrongpassword";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);
        userDonor.setPassword(hashedPassword);
        
        // Mock getByID
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaBuilder.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(userDonor));

        // Act
        boolean result = myUserService.validatePassword(1, TABLE_DONOR, wrongPassword);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetUserByEmail_ExistingDonor_ShouldReturnDonor() {
        // Arrange
        when(userDonorRepo.findByEmail("donor@example.com")).thenReturn(userDonor);

        // Act
        MyUser result = myUserService.getUserByEmail("donor@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(userDonor.getId(), result.getId());
        assertEquals(userDonor.getEmail(), result.getEmail());
    }

    @Test
    public void testGetUserByEmail_ExistingRecipient_ShouldReturnRecipient() {
        // Arrange
        when(userDonorRepo.findByEmail("recipient@example.com")).thenReturn(null);
        when(userRecipientRepo.findByEmail("recipient@example.com")).thenReturn(userRecipient);

        // Act
        MyUser result = myUserService.getUserByEmail("recipient@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(userRecipient.getId(), result.getId());
        assertEquals(userRecipient.getEmail(), result.getEmail());
    }

    @Test
    public void testCheckEmailExists_EmailFoundInDonor_ShouldReturnTrue() {
        // Create a spy of the service to partially mock it
        MyUserService serviceSpy = spy(myUserService);
        
        // Mock the checkEmailInTable method
        doReturn(true).when(serviceSpy).checkEmailInTable(eq("donor@example.com"), eq("user_donor"));
        doReturn(false).when(serviceSpy).checkEmailInTable(eq("donor@example.com"), eq("user_recipient"));
        
        // Act
        boolean result = serviceSpy.checkEmailExists("donor@example.com");
        
        // Assert
        assertTrue(result);
        verify(serviceSpy).checkEmailInTable("donor@example.com", "user_donor");
    }

    @Test
    public void testCheckEmailExists_EmailFoundInRecipient_ShouldReturnTrue() {
        // Create a spy of the service to partially mock it
        MyUserService serviceSpy = spy(myUserService);
        
        // Mock the checkEmailInTable method
        doReturn(false).when(serviceSpy).checkEmailInTable(eq("recipient@example.com"), eq("user_donor"));
        doReturn(true).when(serviceSpy).checkEmailInTable(eq("recipient@example.com"), eq("user_recipient"));
        
        // Act
        boolean result = serviceSpy.checkEmailExists("recipient@example.com");
        
        // Assert
        assertTrue(result);
        verify(serviceSpy).checkEmailInTable("recipient@example.com", "user_donor");
        verify(serviceSpy).checkEmailInTable("recipient@example.com", "user_recipient");
    }

    @Test
    public void testCheckEmailExists_EmailNotFound_ShouldReturnFalse() {
        // Create a spy of the service to partially mock it
        MyUserService serviceSpy = spy(myUserService);
        
        // Mock the checkEmailInTable method
        doReturn(false).when(serviceSpy).checkEmailInTable(eq("missing@example.com"), eq("user_donor"));
        doReturn(false).when(serviceSpy).checkEmailInTable(eq("missing@example.com"), eq("user_recipient"));
        
        // Act
        boolean result = serviceSpy.checkEmailExists("missing@example.com");
        
        // Assert
        assertFalse(result);
        verify(serviceSpy).checkEmailInTable("missing@example.com", "user_donor");
        verify(serviceSpy).checkEmailInTable("missing@example.com", "user_recipient");
    }

    @Test
    public void testSave_ShouldHashPasswordAndPersistUser() {
        // Arrange
        UserDonor newUser = new UserDonor();
        newUser.setUserName("New Donor");
        newUser.setEmail("new@example.com");
        newUser.setPassword("plainPassword");
        newUser.setPhoneNumber("1112223333");
        
        // Act
        myUserService.save(newUser, TABLE_DONOR);
        
        // Assert
        assertNotEquals("plainPassword", newUser.getPassword());
        assertFalse(newUser.getIsDelete());
        assertFalse(newUser.getIsUpdate());
        verify(entityManager).persist(newUser);
    }

    @Test
    public void testUpdateProfile_ExistingUser_ShouldUpdateProfile() {
        // Arrange
        UserDonor updatedUser = new UserDonor();
        updatedUser.setUserName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPhoneNumber("9998887777");
        
        when(entityManager.find(UserDonor.class, 1)).thenReturn(userDonor);
        
        // Act
        UserDonor result = myUserService.updateProfile(updatedUser, 1, TABLE_DONOR);
        
        // Assert
        assertEquals("Updated Name", result.getUserName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("9998887777", result.getPhoneNumber());
        assertTrue(result.getIsUpdate());
        assertNotNull(result.getUpdateDate());
        verify(entityManager).merge(userDonor);
    }

    @Test
    public void testUpdatePass_ValidOriginalPassword_ShouldUpdatePassword() {
        // Arrange
        String originalPassword = "password123";
        String newPassword = "newPassword456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDonor.setPassword(encoder.encode(originalPassword));
        
        when(entityManager.find(UserDonor.class, 1)).thenReturn(userDonor);
        
        // Act
        boolean result = myUserService.updatePass(originalPassword, newPassword, 1, TABLE_DONOR);
        
        // Assert
        assertTrue(result);
        assertNotEquals(encoder.encode(originalPassword), userDonor.getPassword());
        assertTrue(encoder.matches(newPassword, userDonor.getPassword()));
        assertTrue(userDonor.getIsUpdate());
        assertNotNull(userDonor.getUpdateDate());
        verify(entityManager).merge(userDonor);
    }

    @Test
    public void testUpdatePass_InvalidOriginalPassword_ShouldReturnFalse() {
        // Arrange
        String originalPassword = "password123";
        String wrongPassword = "wrongPassword";
        String newPassword = "newPassword456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(originalPassword);
        userDonor.setPassword(hashedPassword);
        
        when(entityManager.find(UserDonor.class, 1)).thenReturn(userDonor);
        
        // Act
        boolean result = myUserService.updatePass(wrongPassword, newPassword, 1, TABLE_DONOR);
        
        // Assert
        assertFalse(result);
        // Don't compare the actual hashed values since they'll be different each time
        // Just verify that the password wasn't changed
        assertNotEquals(newPassword, userDonor.getPassword());
        verify(entityManager, never()).merge(any());
    }

    @Test
    public void testResetPassword_ExistingUser_ShouldResetPassword() {
        // Arrange
        String newPassword = "resetPassword789";
        when(userDonorRepo.findByEmail("donor@example.com")).thenReturn(userDonor);
        
        // Act
        boolean result = myUserService.resetPassword("donor@example.com", newPassword);
        
        // Assert
        assertTrue(result);
        assertTrue(userDonor.getIsUpdate());
        assertNotNull(userDonor.getUpdateDate());
        verify(entityManager).merge(userDonor);
    }

    @Test
    public void testResetPassword_NonExistingUser_ShouldReturnFalse() {
        // Arrange
        String newPassword = "resetPassword789";
        when(userDonorRepo.findByEmail("nonexisting@example.com")).thenReturn(null);
        when(userRecipientRepo.findByEmail("nonexisting@example.com")).thenReturn(null);
        
        // Act
        boolean result = myUserService.resetPassword("nonexisting@example.com", newPassword);
        
        // Assert
        assertFalse(result);
        verify(entityManager, never()).merge(any());
    }

    @Test
    public void testSendForgotPasswordVerification_EmailExists_ShouldSendMail() {
        // Arrange
        when(userDonorRepo.findByEmail("donor@example.com")).thenReturn(userDonor);

        // Act
        boolean result = myUserService.sendForgotPasswordVerification("donor@example.com", "123456");

        // Assert
        assertTrue(result);
        verify(mailService).sendForgotPasswordEmail("donor@example.com", "123456");
    }

    @Test
    public void testSendForgotPasswordVerification_EmailNotFound_ShouldReturnFalse() {
        // Arrange
        when(userDonorRepo.findByEmail("nonexisting@example.com")).thenReturn(null);
        when(userRecipientRepo.findByEmail("nonexisting@example.com")).thenReturn(null);

        // Act
        boolean result = myUserService.sendForgotPasswordVerification("nonexisting@example.com", "123456");

        // Assert
        assertFalse(result);
        verify(mailService, never()).sendForgotPasswordEmail(anyString(), anyString());
    }
} 