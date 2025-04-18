package e_was.backend.Service.Organization;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.Model.Organization.MyOrganization;
import e_was.backend.Model.Organization.MyOrganizationStat;
import e_was.backend.Model.Rewards.MyRewards;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyOrganizationStatsService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrganizationStatsTable organizationStatsTable;

    // Get All
    public <T extends MyOrganizationStat> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) organizationStatsTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }
    
    // Get by ID
    public <T extends MyOrganizationStat> T getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) organizationStatsTable.getEntity(tableName);

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
    public MyOrganizationStat save(MyOrganizationStat organization, String tableName){    
        organization.setIsDelete(false);
        organization.setIsUpdate(false);

        entityManager.persist(organization);
        return organization;
    }

    // update record
    public MyOrganizationStat update(MyOrganizationStat organization, int id, String tableName){
        Class<? extends MyOrganizationStat> entityClass = organizationStatsTable.getEntity(tableName);


        MyOrganizationStat existOrganization = entityManager.find(entityClass, id);
        if (existOrganization !=null) {
            existOrganization.setCollected(existOrganization.getCollected() + organization.getCollected());           
            existOrganization.setProcessed(existOrganization.getProcessed() + organization.getProcessed());
            existOrganization.setRecycled(existOrganization.getRecycled() + organization.getRecycled());
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
        Class<? extends MyOrganizationStat> entityClass = organizationStatsTable.getEntity(tableName);
        MyOrganizationStat existOrganization = entityManager.find(entityClass, id);
        if (existOrganization == null) {
            throw new RuntimeException("Organization not found");
        } else {
            existOrganization.setIsDelete(true);
            existOrganization.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existOrganization);
        }
    }

}
