package e_was.backend.Service.PickupItem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_was.backend.DTO.PickupItemDTO;
import e_was.backend.Model.PickupItem.MyPickupItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyPickupItemService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ItemTable itemTable;

    // Get All
    public <T extends MyPickupItem> List<T> getAll(String tableName){
        Class<T> entityClass = (Class<T>) itemTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.equal(root.get("isDelete"), false));
        return entityManager.createQuery(query).getResultList();
    }
    
    // Get by user ID
    public <T extends MyPickupItem> List<T> getByID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) itemTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("userDonorID"), id),
                cb.in(root.get("itemStatusID")).value(1).value(2),
                cb.equal(root.get("isDelete"), false)
            )
        );
        return entityManager.createQuery(query).getResultList();
    }

    // Get by item ID
    public <T extends MyPickupItem> Optional<T> getByItemID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) itemTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("pickupItemID"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );

        List<T> resultList = entityManager.createQuery(query).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));    
    }

    
    // Get multiple by item ID
    public <T extends MyPickupItem> List<T> getByItemsID(int id, String tableName) {
        Class<T> entityClass = (Class<T>) itemTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("pickupItemID"), id),
                cb.equal(root.get("isDelete"), false)
            )
        );

        return entityManager.createQuery(query).getResultList();
    }

        // Get history multiple by item ID
    public <T extends MyPickupItem> List<T> getHistoryItem(int id, String tableName) {
        Class<T> entityClass = (Class<T>) itemTable.getEntity(tableName);
    
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(
            cb.and(
                cb.equal(root.get("userDonorID"), id),
                cb.in(root.get("itemStatusID")).value(3).value(4),
                cb.equal(root.get("isDelete"), false)
            )
        );
    
        return entityManager.createQuery(query).getResultList();
    }

        
    // Add new
    public MyPickupItem save(MyPickupItem pickupItem, String tableName){    
        pickupItem.setItemStatusID(1);
        entityManager.persist(pickupItem);
        return pickupItem;
        
    }

    // update record
    public int update(MyPickupItem item, int id, String tableName, String oldName){
        Class<? extends MyPickupItem> entityClass = itemTable.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<MyPickupItem> update = cb.createCriteriaUpdate(MyPickupItem.class);
        Root<MyPickupItem> root = update.from(MyPickupItem.class);

        update.set("itemName", item.getItemName());
        update.set("itemTypeID", item.getItemTypeID());
        update.set("deviceConditionID", item.getDeviceConditionID());
        update.set("dimensionLength", item.getDimensionLength());
        update.set("dimensionWidth", item.getDimensionWidth());
        update.set("dimensionHeight", item.getDimensionHeight());
        update.set("pickupLocation", item.getPickupLocation());
        update.set("isUpdate", true);
        update.set("updateDate", new Timestamp(System.currentTimeMillis()));
        
        update.where(
        cb.and(
            cb.equal(root.get("userDonorID"), id),
            cb.equal(root.get("itemName"), oldName),
            cb.equal(root.get("itemStatusID"), 1), 
            cb.equal(root.get("isDelete"), false)
        ));
    return entityManager.createQuery(update).executeUpdate();
    }

    // Delete
    @Transactional
    public void delete(int id, String tableName) {
        Class<? extends MyPickupItem> entityClass = itemTable.getEntity(tableName);
        MyPickupItem existItem = entityManager.find(entityClass, id);
        if (existItem == null) {
            throw new RuntimeException("Item not found");
        }
        existItem.setIsDelete(true);
        existItem.setDeleteDate(new Timestamp(System.currentTimeMillis()));
        entityManager.merge(existItem);
    }
    
}
