package e_was.backend.Service.PickupTransaction;

import java.sql.Timestamp;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.Model.PickupItem.MyPickupItem;
import e_was.backend.Model.PickupTransaction.MyTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyTransactionService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTable transactionTable;

    // Get All
    public <T extends MyTransaction> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) transactionTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }
    
    // Get by ID
    public <T extends MyTransaction> List<T> getByID(int id, int orgID, String tableName) {
        Class<T> entityClass = (Class<T>) transactionTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("userDonorID"), id),
                cb.equal(root.get("organizationID"), orgID),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }

    // Get by org 
    public <T extends MyTransaction> List<T> getDistinctOrgIDsByDonorID(int id, String tableName) {
        Class<T> entityClass =(Class<T>) transactionTable.getEntity(tableName);
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        
        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("userDonorID"), id),
                 cb.equal(root.get("isDelete"), false)
             )).groupBy(root.get("organizationID"));
        return entityManager.createQuery(query).getResultList();
    }

    // Get by org 
    public <T extends MyTransaction> List<T> getHistory(int id, String tableName) {
        Class<T> entityClass =(Class<T>) transactionTable.getEntity(tableName);
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        
        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("userDonorID"), id),
                 cb.in(root.get("pickupStatusID")).value(3).value(4),
                 cb.equal(root.get("isDelete"), false)
             )).groupBy(root.get("organizationID"));
        return entityManager.createQuery(query).getResultList();
    }

    // Add new
    public MyTransaction save(MyTransaction transaction, String tableName){    
        transaction.setPickupStatusID(1);
        entityManager.persist(transaction);
        return transaction;
        
    }

    // update record
    public MyTransaction update(MyTransaction transaction, int id, String tableName){
        Class<? extends MyTransaction> entityClass = transactionTable.getEntity(tableName);

        MyTransaction existTransaction = entityManager.find(entityClass, id);
        if (existTransaction !=null) {
            existTransaction.setPickupItemID(transaction.getPickupItemID());
            existTransaction.setUserDonorID(transaction.getUserDonorID());
            existTransaction.setUserRecipientID(transaction.getUserRecipientID());
            existTransaction.setIsUpdate(true);
            existTransaction.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            entityManager.merge(existTransaction);
            return existTransaction;
        } else {
            throw new RuntimeException("Pickup transaction not found");
        }
    }

    // Delete
    @Transactional
    public void delete(int id, String tableName) {
        Class<? extends MyTransaction> entityClass = transactionTable.getEntity(tableName);
        MyTransaction existTransaction = entityManager.find(entityClass, id);
        if (existTransaction == null) {
            throw new RuntimeException("Pickup transaction not found");
        }
        existTransaction.setIsDelete(true);
        existTransaction.setDeleteDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(existTransaction);
    }
}
