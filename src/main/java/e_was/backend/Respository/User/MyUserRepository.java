package e_was.backend.Respository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import e_was.backend.Model.User.MyUser;

@Repository
public interface MyUserRepository<T extends MyUser> extends JpaRepository<T, Integer>{
    
}
