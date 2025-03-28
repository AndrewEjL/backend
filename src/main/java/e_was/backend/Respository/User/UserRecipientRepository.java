package e_was.backend.Respository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.User.UserRecipient;

@Repository
public interface UserRecipientRepository extends JpaRepository<UserRecipient, Integer> {
    
}
