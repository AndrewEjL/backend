package e_was.backend.Service.User;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import e_was.backend.Model.User.UserDonor;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckEmailExists_EmailFoundInDonor_ShouldReturnTrue() {
        when(userDonorRepo.findByEmail("test@example.com")).thenReturn(new UserDonor());

        boolean result = myUserService.checkEmailExists("test@example.com");

        assertTrue(result);
    }

    @Test
    public void testCheckEmailExists_EmailNotFound_ShouldReturnFalse() {
        when(userDonorRepo.findByEmail("missing@example.com")).thenReturn(null);
        when(userRecipientRepo.findByEmail("missing@example.com")).thenReturn(null);

        boolean result = myUserService.checkEmailExists("missing@example.com");

        assertFalse(result);
    }

    @Test
    public void testSendForgotPasswordVerification_EmailExists_ShouldSendMail() {
        UserDonor user = new UserDonor();
        user.setEmail("test@example.com");

        when(userDonorRepo.findByEmail("test@example.com")).thenReturn(user);

        boolean result = myUserService.sendForgotPasswordVerification("test@example.com", "123456");

        verify(mailService, times(1)).sendForgotPasswordEmail("test@example.com", "123456");
        assertTrue(result);
    }

    @Test
    public void testSendForgotPasswordVerification_EmailNotFound_ShouldReturnFalse() {
        when(userDonorRepo.findByEmail("missing@example.com")).thenReturn(null);
        when(userRecipientRepo.findByEmail("missing@example.com")).thenReturn(null);

        boolean result = myUserService.sendForgotPasswordVerification("missing@example.com", "123456");

        assertFalse(result);
    }
}
