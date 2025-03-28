package e_was.backend.Service.Organization;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.Model.Organization.MyOrganization;
import e_was.backend.Model.User.MyUser;
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

    // Add new
    public MyOrganization save(MyOrganization organization, String tableName){    
        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pass = RandomStringUtils.random(7, character);
        organization.setPassword(pass);
        organization.setIsDelete(false);
        organization.setIsUpdate(false);

        entityManager.persist(organization);
        return organization;
    }

    // update record
    public MyOrganization update(MyOrganization organization, int id, String tableName){
        Class<? extends MyOrganization> entityClass = organizationTable.getEntity(tableName);

        MyOrganization existOrganization = entityManager.find(entityClass, id);
        if (existOrganization !=null) {
            existOrganization.setOrganizationName(organization.getOrganizationName());
            existOrganization.setRegistrationNumber(organization.getRegistrationNumber());
            existOrganization.setRegistrationType(organization.getRegistrationType());
            existOrganization.setPhoneNumber(organization.getPhoneNumber());            
            existOrganization.setAddress(organization.getAddress());
            existOrganization.setEmail(organization.getEmail());
            existOrganization.setPassword(organization.getPassword());
            existOrganization.setIsUpdate(true);
            existOrganization.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existOrganization);
            return existOrganization;
        } else {
            throw new RuntimeException("Organization not found");
        }
    }

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
                cb.equal(root.get("password"), password),
                cb.equal(root.get("isDelete"), false)
            )
        );
    
        List<MyOrganization> result = entityManager.createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}
