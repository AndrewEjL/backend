package e_was.backend.Respository.StatusRespository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.StatusModel.MyStatus;

@Repository
public interface MyStatusRepository<T extends MyStatus> extends JpaRepository<T, Integer> {
}


