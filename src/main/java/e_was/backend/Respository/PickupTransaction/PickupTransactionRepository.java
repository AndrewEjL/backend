package e_was.backend.Respository.PickupTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.PickupTransaction.MyTransaction;

@Repository
public interface PickupTransactionRepository extends JpaRepository<MyTransaction, Integer>{
    
}
