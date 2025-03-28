package e_was.backend.Service.StatusService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import e_was.backend.Model.StatusModel.MyStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MyStatusService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TableService tableService;

    // Get All
    public <T extends MyStatus> List<T> getAll(String tableName) {
        Class<T> entityClass = (Class<T>) tableService.getEntity(tableName);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass); 
        Root<T> root = query.from(entityClass);
        query.select(root); 
        return entityManager.createQuery(query).getResultList();
    }


    // Get by ID
    public MyStatus getByID(int id, String tableName) {
        Class<? extends MyStatus> entityClass = tableService.getEntity(tableName);
        return entityManager.find(entityClass, id);
    }

    // Add new data
    public MyStatus save(MyStatus status, String tableName) {
        entityManager.persist(status);
        return status;
    }

    // Update data
    public MyStatus update(MyStatus status, int id, String tableName) {
        Class<? extends MyStatus> entityClass = tableService.getEntity(tableName);
        MyStatus existing = entityManager.find(entityClass, id);
        if (existing != null) {
            existing.setName(status.getName());
            entityManager.merge(existing);
            return existing;
        } else {
            throw new RuntimeException("Status ID " + id + " not found");
        }
    }

    // Delete by ID
    public void delete(int id, String tableName) {
        Class<? extends MyStatus> entityClass = tableService.getEntity(tableName);
        MyStatus entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        } else {
            throw new RuntimeException("Status ID " + id + " not found");
        }
    }

}
