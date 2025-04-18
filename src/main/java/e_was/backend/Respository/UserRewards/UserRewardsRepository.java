package e_was.backend.Respository.UserRewards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.UserRewards.MyUserRewards;

@Repository
public interface UserRewardsRepository extends JpaRepository<MyUserRewards, Integer>{
    
}
