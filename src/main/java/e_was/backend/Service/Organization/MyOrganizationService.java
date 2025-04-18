package e_was.backend.Service.Organization;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import e_was.backend.PasswordUtil;
import e_was.backend.Model.Organization.MyOrganization;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
import e_was.backend.Respository.Organization.OrganizationRepository;
import e_was.backend.Service.email.MailService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyOrganizationService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrganizationTable organizationTable;

    @Autowired
    private MailService mailService;

    @Autowired
    private OrganizationRepository orgRepo;

    // Get All
    public <T extends MyOrganization> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) organizationTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }
    
    // Get by ID
    public <T extends MyOrganization> T getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) organizationTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("organizationID"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );
        List<T> result = entityManager.createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // validate password
    public boolean validatePassword(int id, String tableName, String inputPass) {
        MyOrganization org = getByID(id, tableName);
        if(org == null){
            return false;
        }
           
        String hashedPassword = org.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(inputPass, hashedPassword);
    }

    // Add new
    public MyOrganization save(MyOrganization organization, String tableName){    
        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pass = RandomStringUtils.random(7, character);
        String hashPass = PasswordUtil.hashPassword(pass);
        // organization.setPassword(hashPass);
        organization.setPassword(hashPass);
        organization.setIsDelete(false);
        organization.setIsUpdate(false);

        entityManager.persist(organization);

        // permanent function because no admin panel to press button to send email 
        try {
            mailService.sendRegistrationPassword(organization.getEmail(), pass);
            mailService.sendOrganizationRejection(organization.getEmail());
        } catch(MessagingException e) {
            throw new RuntimeException("Failed to send email with password", e);
        }
        return organization;
    }

    // update record
    public MyOrganization update(MyOrganization organization, int id, String tableName){
        Class<? extends MyOrganization> entityClass = organizationTable.getEntity(tableName);

        MyOrganization existOrganization = entityManager.find(entityClass, id);
        if (existOrganization !=null) {
            existOrganization.setPhoneNumber(organization.getPhoneNumber());            
            existOrganization.setAddress(organization.getAddress());
            existOrganization.setEmail(organization.getEmail());
            existOrganization.setIsUpdate(true);
            existOrganization.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existOrganization);
            return existOrganization;
        } else {
            throw new RuntimeException("Organization not found");
        }
    }

    //Update password record
    public boolean updatePass(String originPassword, String newPassword, int id, String tableName) {
        Class<? extends MyOrganization> entityClass = organizationTable.getEntity(tableName);
        MyOrganization org = entityManager.find(entityClass, id);

        if (org == null || org.getIsDelete()) {
            return false;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean match = encoder.matches(originPassword, org.getPassword());
        if(!match){
            return false;
        }

        String hashedNewPass = encoder.encode(newPassword);
    
        org.setPassword(hashedNewPass);
        org.setIsUpdate(true);
        org.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(org);
    
        return true;
    }

    // // Update password record
    // public <T extends MyOrganization> T updatePass(String newPassword, int id, String tableName) {
    //     Class<T> entityClass = (Class<T>) organizationTable.getEntity(tableName);
            
    //     T existUser = entityManager.find(entityClass, id);
    //     if (existUser != null) {
    //         String hashPass = PasswordUtil.hashPassword(newPassword);
    //         existUser.setPassword(hashPass);
    //         existUser.setIsUpdate(true);
    //         existUser.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    //         entityManager.merge(existUser);
    //         return existUser;
    //     } else {
    //         throw new RuntimeException("User not found");
    //     }
    // }

    // Delete
    public void delete(int id, String tableName){
        Class<? extends MyOrganization> entityClass = organizationTable.getEntity(tableName);
        MyOrganization existOrganization = entityManager.find(entityClass, id);
        if (existOrganization == null) {
            throw new RuntimeException("Organization not found");
        } else {
            existOrganization.setIsDelete(true);
            existOrganization.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existOrganization);
        }
    }

    // Check Email
    public boolean checkEmailExists(String email){
        boolean existInOrganization = checkEmailInTable(email, "organization");

        return existInOrganization;
    }

    public boolean checkEmailInTable(String email, String tableName){
    Class<? extends MyOrganization> entityClass = (Class<? extends MyOrganization>) organizationTable.getEntity(tableName);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(long.class);
    Root<? extends MyOrganization> root = query.from(entityClass);
    query.select(cb.count(root)).where(
        cb.and(
            cb.equal(root.get("email"), email),
            cb.equal(root.get("isDelete"), false)
        ));
    Long count = entityManager.createQuery(query).getSingleResult();
    return count>0;
    }

    //login
    public MyOrganization login(String email, String password) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MyOrganization> query = cb.createQuery(MyOrganization.class);
        Root<MyOrganization> root = query.from(MyOrganization.class);
    
        query.select(root).where(
            cb.and(
                cb.equal(root.get("email"), email),
                cb.equal(root.get("isDelete"), false)
            )
        );
    
        List<MyOrganization> result = entityManager.createQuery(query).getResultList();
        if (result.isEmpty()) return null;

        MyOrganization org = result.get(0);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, org.getPassword())) {
            return org;
        }

        return null;
    }

        // //login
        // public MyOrganization login(String email, String password) {
        //     CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        //     CriteriaQuery<MyOrganization> query = cb.createQuery(MyOrganization.class);
        //     Root<MyOrganization> root = query.from(MyOrganization.class);
        
        //     query.select(root).where(
        //         cb.and(
        //             cb.equal(root.get("email"), email),
        //             cb.equal(root.get("password"), password),
        //             cb.equal(root.get("isDelete"), false)
        //         )
        //     );
        
        //     List<MyOrganization> result = entityManager.createQuery(query).getResultList();
        //     return result.isEmpty() ? null : result.get(0);
        // }

    // Get user by email 
    public MyOrganization getOrgByEmail(String email) {
        // Check organization table
        MyOrganization org = orgRepo.findByEmail(email);
        if (org != null) return org;
    
        return null;
    }
    
    // send email for forgot password
    public boolean sendForgotPasswordVerification(String email, String code) {
        MyOrganization org = getOrgByEmail(email); 
        if (org == null) return false;
    
        mailService.sendForgotPasswordEmail(email, code);
        return true;
    }

    // reset password
    public boolean resetPassword(String email, String newPassword) {
        MyOrganization org = getOrgByEmail(email);
        if (org == null) return false;
    
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        org.setPassword(encoder.encode(newPassword));
        org.setIsUpdate(true);
        org.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    
        entityManager.merge(org);
        return true;
    }

}
