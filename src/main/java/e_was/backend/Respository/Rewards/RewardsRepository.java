package e_was.backend.Respository.Rewards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.Rewards.MyRewards;

@Repository
public interface RewardsRepository extends JpaRepository<MyRewards, Integer>{
    
}
