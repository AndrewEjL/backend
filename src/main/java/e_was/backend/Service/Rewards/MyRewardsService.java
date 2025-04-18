package e_was.backend.Service.Rewards;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.Model.PickupTransaction.MyTransaction;
import e_was.backend.Model.Rewards.MyRewards;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyRewardsService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RewardTableService rewardTableService;

    // Get All
    public <T extends MyRewards> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) rewardTableService.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }

    // Get by ID
    public <T extends MyRewards> List<T> getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) rewardTableService.getEntity(tableName);
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("rewardsID"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }    

    // Get by rewards ID
    public <T extends MyRewards> List<T> getByRewardsID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) rewardTableService.getEntity(tableName);
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("rewardsType"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }    

    // Get by rewards points
    public <T extends MyRewards> List<T> getByRewardsPoints(int minPoints, int maxPoints, String tableName) {
        Class<T> entityClass = (Class<T>) rewardTableService.getEntity(tableName);
            
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.greaterThanOrEqualTo(root.get("point_needed"), minPoints),
                cb.lessThanOrEqualTo(root.get("point_needed"), maxPoints),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }
    
    // Add new
    public MyRewards save(MyRewards rewards, String tableName){    
        entityManager.persist(rewards);
        return rewards;        
    }

    // update record
    public MyRewards update(MyRewards rewards, int id, String tableName){
        Class<? extends MyRewards> entityClass = rewardTableService.getEntity(tableName);

        MyRewards existRewards = entityManager.find(entityClass, id);
        if (existRewards !=null) {
            existRewards.setRewardsName(rewards.getRewardsName());
            existRewards.setRewardsImage(rewards.getRewardsImage());
            existRewards.setRewardsType(rewards.getRewardsType());
            existRewards.setDescription(rewards.getDescription());
            existRewards.setPointNeeded(rewards.getPointNeeded());
            existRewards.setQuantity(rewards.getQuantity());
            existRewards.setIsUpdate(true);
            existRewards.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existRewards);
            return existRewards;
        } else {
            throw new RuntimeException("Rewards not found");
        }
    }

    // update record
    public MyRewards updateQuantity(MyRewards rewards, int id, String tableName){
        Class<? extends MyRewards> entityClass = rewardTableService.getEntity(tableName);
    
        MyRewards existRewards = entityManager.find(entityClass, id);
        if (existRewards !=null) {
            existRewards.setQuantity(rewards.getQuantity());
            if (rewards.getQuantity() == 0) {
                existRewards.setRewardsStatus(2);
            }
            existRewards.setDescription(existRewards.getDescription());
            existRewards.setIsUpdate(true);
            existRewards.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existRewards);
            return existRewards;
        } else {
            throw new RuntimeException("Rewards not found");
        }
    }

    // Delete
    @Transactional
    public void delete(int id, String tableName) {
        Class<? extends MyRewards> entityClass = rewardTableService.getEntity(tableName);
        MyRewards existRewards = entityManager.find(entityClass, id);
        if (existRewards == null) {
            throw new RuntimeException("Rewards not found");
        }
        existRewards.setIsDelete(true);
        existRewards.setDeleteDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(existRewards);
    }
}
