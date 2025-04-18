package e_was.backend.Respository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.User.UserDonor;

@Repository
public interface UserDonorRepository extends JpaRepository<UserDonor, Integer> {
    UserDonor findByEmail(String email);
}

