package e_was.backend.Service.User;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
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

    // Add new
    public <T extends MyUser> T save(T user, String tableName) {
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
    public <T extends MyUser> T updatePass(String newPassword, int id, String tableName) {
        Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);
        
        T existUser = entityManager.find(entityClass, id);
        if (existUser != null) {
            existUser.setPassword(newPassword); // Just update password
            existUser.setIsUpdate(true);
            existUser.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existUser);
            return existUser;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //updatePoint
    // public <T extends MyUser> T updatePoint(T user, int id, String tableName) {
    //     Class<T> entityClass = (Class<T>) userTable.getEntity(tableName);

    //     T existUser = entityManager.find(entityClass, id);
    //     if (existUser != null) {
    //         existUser.setRewardPoints(user.getRewardPoints);
    //         existUser.setIsUpdate(true);
    //         existUser.setUpdateDate(new Timestamp(System.currentTimeMillis()));
    //         entityManager.merge(existUser);
    //         return existUser;
    //     } else {
    //         throw new RuntimeException("User not found");
    //     }
    // }

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
                cb.equal(root.get("password"), password),
                cb.equal(root.get("isDelete"), false)
            )
        );
    
        List<? extends MyUser> result = entityManager.createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}