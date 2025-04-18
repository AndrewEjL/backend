package e_was.backend.Service.User;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import e_was.backend.PasswordUtil;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
import e_was.backend.Respository.User.UserDonorRepository;
import e_was.backend.Respository.User.UserRecipientRepository;
import e_was.backend.Service.email.MailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyUserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserTable userTable;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserDonorRepository userDonorRepo;

    @Autowired
    private UserRecipientRepository userRecipientRepo;

    // Get All
    public <T extends MyUser> List<T> getAll(String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }

    // Get by ID
    public <T extends MyUser> T getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("id"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );
        List<T> result = entityManager.createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // validate password
    public boolean validatePassword(int id, String tableName, String inputPass) {
        MyUser user = getByID(id, tableName);
        if(user == null){
            return false;
        }
           
        String hashedPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(inputPass, hashedPassword);
    }

    //Get by orgID
    public <T extends MyUser> List<T> getByOrgID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("organizationID"), id)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }

    // Get user by email 
    public MyUser getUserByEmail(String email) {
        // Check donor table
        UserDonor donor = userDonorRepo.findByEmail(email);
        if (donor != null) return donor;
    
        // Check recipient table
        UserRecipient recipient = userRecipientRepo.findByEmail(email);
        if (recipient != null) return recipient;
    
        return null;
    }
    

    // Add new
    public <T extends MyUser> T save(T user, String tableName) {
        String hassPass = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hassPass);
        user.setIsDelete(false);
        user.setIsUpdate(false);
        entityManager.persist(user);
        return user;
    }

    // Update profile record
    public <T extends MyUser> T updateProfile(T user, int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        T existUser = entityManager.find(entityClass, id);
        if (existUser != null) {
            existUser.setUserName(user.getUserName());
            existUser.setEmail(user.getEmail());
            existUser.setPhoneNumber(user.getPhoneNumber());
            existUser.setIsUpdate(true);
            existUser.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existUser);
            return existUser;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Update password record
    public boolean updatePass(String originPassword, String newPassword, int id, String tableName) {
        Class<? extends MyUser> entityClass = userTable.getEntity(tableName);
        MyUser user = entityManager.find(entityClass, id);
        if (user == null || user.getIsDelete()) return false;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean match = encoder.matches(originPassword, user.getPassword());

        if (!match) return false;

        String hashedNewPass = encoder.encode(newPassword);
        user.setPassword(hashedNewPass);
        user.setIsUpdate(true);
        user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(user);

        return true;
    }

    // reset password
    public boolean resetPassword(String email, String newPassword) {
        MyUser user = getUserByEmail(email);
        if (user == null) return false;
    
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));
        user.setIsUpdate(true);
        user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    
        entityManager.merge(user);
        return true;
    }
    

    // updatePoint
    public <T extends MyUser> T updatePoint(T user, int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);
    
        T existingUser = entityManager.find(entityClass, id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
    
        if (existingUser instanceof UserDonor && user instanceof UserDonor) {
            UserDonor donor = (UserDonor) existingUser;
            UserDonor updatedUser = (UserDonor) user;
    
            donor.setRewardPoints(updatedUser.getRewardPoints());
            donor.setIsUpdate(true);
            donor.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    
            entityManager.merge(donor);
        } else {
            throw new IllegalArgumentException("Reward points update only supported for UserDonor.");
        }
    
        return existingUser;
    }
    
    // Delete
    public <T extends MyUser> void delete(int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        T existUser = entityManager.find(entityClass, id);
        if (existUser == null) {
            throw new RuntimeException("User not found");
        } else {
            existUser.setUserStatusID(2);
            existUser.setIsDelete(true);
            existUser.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existUser);
        }
    }

    // Restore Collector
    public <T extends MyUser> void restoreCollector(int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

        T existUser = entityManager.find(entityClass, id);
        if (existUser == null) {
            throw new RuntimeException("User not found");
        } else {
            existUser.setUserStatusID(1);
            existUser.setIsDelete(false);
            existUser.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existUser);
        }
    }

    // Check email
    public boolean checkEmailExists(String email){
        boolean existInDonor = checkEmailInTable(email, "user_donor");
        boolean existInRecipient = checkEmailInTable(email, "user_recipient");

        return existInDonor || existInRecipient;
    }

    public boolean checkEmailInTable(String email, String tableName){
        Class<? extends MyUser> entityClass = (Class<? extends MyUser>) userTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(long.class);
        Root<? extends MyUser> root = query.from(entityClass);
        query.select(cb.count(root)).where(
            cb.and(
                cb.equal(root.get("email"), email),
                cb.equal(root.get("isDelete"), false)
            ));
        Long count = entityManager.createQuery(query).getSingleResult();
        return count>0;
    }

    // login
    public MyUser login(String tableName, String email, String password) {
        Class<? extends MyUser> entityClass = userTable.getEntity(tableName);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MyUser> query = (CriteriaQuery<MyUser>) cb.createQuery(entityClass);
        Root<? extends MyUser> root = query.from(entityClass);
    
        query.select(root).where(
            cb.and(
                cb.equal(root.get("email"), email),
                cb.equal(root.get("isDelete"), false)
            )
        );
    
        List<? extends MyUser> result = entityManager.createQuery(query).getResultList();
        if (result.isEmpty()) return null;

        MyUser user = result.get(0);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // send email for forgot password
    public boolean sendForgotPasswordVerification(String email, String code) {
        MyUser user = getUserByEmail(email); // checks donor & recipient
        if (user == null) return false;
    
        mailService.sendForgotPasswordEmail(email, code);
        return true;
    }
    
    
}