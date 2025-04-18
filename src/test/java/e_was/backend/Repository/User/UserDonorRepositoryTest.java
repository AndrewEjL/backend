package e_was.backend.Repository.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import e_was.backend.Model.User.UserDonor;
import e_was.backend.Respository.User.UserDonorRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test") // This will use application-test.properties for the test
public class UserDonorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDonorRepository userDonorRepository;

    @Test
    public void testFindByEmail_ExistingEmail_ShouldReturnUser() {
        // Arrange
        UserDonor donor = new UserDonor();
        donor.setUserName("Test Donor");
        donor.setEmail("donor@example.com");
        donor.setPassword("hashedPassword");
        donor.setPhoneNumber("1234567890");
        donor.setUserStatusID(1);
        donor.setIsDelete(false);
        donor.setIsUpdate(false);
        donor.setRewardPoints(100);

        entityManager.persist(donor);
        entityManager.flush();

        // Act
        UserDonor found = userDonorRepository.findByEmail("donor@example.com");

        // Assert
        assertNotNull(found);
        assertEquals("donor@example.com", found.getEmail());
        assertEquals("Test Donor", found.getUserName());
    }

    @Test
    public void testFindByEmail_NonExistingEmail_ShouldReturnNull() {
        // Act
        UserDonor found = userDonorRepository.findByEmail("nonexisting@example.com");

        // Assert
        assertNull(found);
    }

    @Test
    public void testSave_NewUser_ShouldPersistUser() {
        // Arrange
        UserDonor donor = new UserDonor();
        donor.setUserName("New Donor");
        donor.setEmail("new.donor@example.com");
        donor.setPassword("hashedPassword");
        donor.setPhoneNumber("9876543210");
        donor.setUserStatusID(1);
        donor.setIsDelete(false);
        donor.setIsUpdate(false);
        donor.setRewardPoints(50);

        // Act
        UserDonor saved = userDonorRepository.save(donor);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        
        // Verify it was persisted by fetching it from the repository
        Optional<UserDonor> fetched = userDonorRepository.findById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("new.donor@example.com", fetched.get().getEmail());
        assertEquals("New Donor", fetched.get().getUserName());
        assertEquals(50, fetched.get().getRewardPoints());
    }

    @Test
    public void testFindAll_WithMultipleUsers_ShouldReturnAllUsers() {
        // Arrange
        UserDonor donor1 = new UserDonor();
        donor1.setUserName("Donor One");
        donor1.setEmail("donor1@example.com");
        donor1.setPassword("hashedPassword1");
        donor1.setPhoneNumber("1111111111");
        donor1.setUserStatusID(1);
        donor1.setIsDelete(false);
        donor1.setIsUpdate(false);
        donor1.setRewardPoints(100);

        UserDonor donor2 = new UserDonor();
        donor2.setUserName("Donor Two");
        donor2.setEmail("donor2@example.com");
        donor2.setPassword("hashedPassword2");
        donor2.setPhoneNumber("2222222222");
        donor2.setUserStatusID(1);
        donor2.setIsDelete(false);
        donor2.setIsUpdate(false);
        donor2.setRewardPoints(200);

        entityManager.persist(donor1);
        entityManager.persist(donor2);
        entityManager.flush();

        // Act
        List<UserDonor> donors = userDonorRepository.findAll();

        // Assert
        assertEquals(2, donors.size());
        
        // Verify both users are in the result
        boolean foundDonor1 = false;
        boolean foundDonor2 = false;
        
        for (UserDonor donor : donors) {
            if (donor.getEmail().equals("donor1@example.com")) {
                foundDonor1 = true;
            } else if (donor.getEmail().equals("donor2@example.com")) {
                foundDonor2 = true;
            }
        }
        
        assertTrue(foundDonor1);
        assertTrue(foundDonor2);
    }

    @Test
    public void testDeleteById_ExistingUser_ShouldRemoveUser() {
        // Arrange
        UserDonor donor = new UserDonor();
        donor.setUserName("Temp Donor");
        donor.setEmail("temp@example.com");
        donor.setPassword("hashedPassword");
        donor.setPhoneNumber("3333333333");
        donor.setUserStatusID(1);
        donor.setIsDelete(false);
        donor.setIsUpdate(false);
        donor.setRewardPoints(75);

        donor = entityManager.persist(donor);
        entityManager.flush();
        
        Integer donorId = donor.getId();

        // Verify user exists
        assertTrue(userDonorRepository.findById(donorId).isPresent());

        // Act
        userDonorRepository.deleteById(donorId);

        // Assert
        assertFalse(userDonorRepository.findById(donorId).isPresent());
    }

    @Test
    public void testUpdate_ExistingUser_ShouldUpdateFields() {
        // Arrange
        UserDonor donor = new UserDonor();
        donor.setUserName("Original Name");
        donor.setEmail("original@example.com");
        donor.setPassword("originalPassword");
        donor.setPhoneNumber("4444444444");
        donor.setUserStatusID(1);
        donor.setIsDelete(false);
        donor.setIsUpdate(false);
        donor.setRewardPoints(50);

        donor = entityManager.persist(donor);
        entityManager.flush();
        
        Integer donorId = donor.getId();

        // Act - Update the user
        UserDonor toUpdate = userDonorRepository.findById(donorId).get();
        toUpdate.setUserName("Updated Name");
        toUpdate.setEmail("updated@example.com");
        toUpdate.setPhoneNumber("5555555555");
        toUpdate.setRewardPoints(150);
        toUpdate.setIsUpdate(true);
        toUpdate.setUpdateDate(Timestamp.from(Instant.now()));
        
        userDonorRepository.save(toUpdate);

        // Assert
        UserDonor updated = userDonorRepository.findById(donorId).get();
        assertEquals("Updated Name", updated.getUserName());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("5555555555", updated.getPhoneNumber());
        assertEquals(150, updated.getRewardPoints());
        assertTrue(updated.getIsUpdate());
        assertNotNull(updated.getUpdateDate());
    }
} 