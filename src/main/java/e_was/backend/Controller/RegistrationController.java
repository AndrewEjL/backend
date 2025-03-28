package e_was.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import e_was.backend.Respository.MyAppRespository;
import e_was.backend.Model.MyAppUser;
import jakarta.transaction.Transactional;

@RestController
public class RegistrationController{

    @Autowired
    private MyAppRespository myAppRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping(value = "/signup", consumes = "application/json")
        public MyAppUser createAdmin(@RequestBody MyAppUser user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return myAppRespository.save(user);
        }
}