package e_was.backend.Respository.PickupItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.PickupItem.MyPickupItem;

@Repository
public interface PickupItemRepository extends JpaRepository<MyPickupItem, Integer> {
    
}
