package e_was.backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import e_was.backend.Model.MyAppUser;
import e_was.backend.Respository.MyAppRespository;

@Service
@AllArgsConstructor
public class MyAppService implements UserDetailsManager {

    @Autowired
    private MyAppRespository respository;

    @Override
    public UserDetails loadUserByUsername(String admin_name) throws UsernameNotFoundException {
        Optional<MyAppUser> user = respository.findByAdminName(admin_name);
        if (user.isPresent()) {
            var admin = user.get();
            return User.builder()
                    .username(admin.getAdminName())
                    .password(admin.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(admin_name);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public void createUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public void deleteUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public boolean userExists(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
    
}
