package e_was.backend.Respository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.MyAppUser;

@Repository
public interface MyAppRespository extends JpaRepository<MyAppUser, Integer> {
    Optional<MyAppUser> findByAdminName(String admin_name);
}

