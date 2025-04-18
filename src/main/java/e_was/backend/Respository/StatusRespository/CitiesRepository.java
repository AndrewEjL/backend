package e_was.backend.Respository.StatusRespository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.StatusModel.Cities;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Integer> {
    
}
