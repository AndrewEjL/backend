package e_was.backend.Respository.StatusRespository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.StatusModel.RewardsType;

@Repository
public interface RewardsTypeRepository extends JpaRepository<RewardsType, Integer>{
    
}
