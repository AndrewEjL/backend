package e_was.backend.Service.UserRewards;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.Model.PickupTransaction.MyTransaction;
import e_was.backend.Model.Rewards.MyRewards;
import e_was.backend.Model.UserRewards.MyUserRewards;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyUserRewardsService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRewardTableService userRewardTableService;

    // Get All
    public <T extends MyUserRewards> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) userRewardTableService.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }

    // Get by ID
    public <T extends MyUserRewards> List<T> getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) userRewardTableService.getEntity(tableName);
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("userDonorID"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }      
    
    // Add new
    public MyUserRewards save(MyUserRewards userRewards, String tableName){  
        userRewards.setRewardsUse(false);  
        entityManager.persist(userRewards);
        return userRewards;        
    }

    // update record
    public MyUserRewards update(MyUserRewards userRewards, int id, String tableName){
        Class<? extends MyUserRewards> entityClass = userRewardTableService.getEntity(tableName);

        MyUserRewards existRewards = entityManager.find(entityClass, id);
        if (existRewards !=null) {
            existRewards.setRewardsUse(true);
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
        Class<? extends MyUserRewards> entityClass = userRewardTableService.getEntity(tableName);
        MyUserRewards existRewards = entityManager.find(entityClass, id);
        if (existRewards == null) {
            throw new RuntimeException("Rewards not found");
        }
        existRewards.setIsDelete(true);
        existRewards.setDeleteDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(existRewards);
    }
}
