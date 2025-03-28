package e_was.backend.Controller.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import e_was.backend.BackendApplication;
import e_was.backend.DTO.LoginRequest;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;
import e_was.backend.Service.User.MyUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final BackendApplication backendApplication;

    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private MyUserService myUserService;

    UserController(AuthenticationProvider authenticationProvider, BackendApplication backendApplication) {
        this.authenticationProvider = authenticationProvider;
        this.backendApplication = backendApplication;
    }

    @GetMapping("/{tableName}/all")
    public List<? extends MyUser> getAll(@PathVariable String tableName) {
        return myUserService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public MyUser getByID(@PathVariable String tableName, @PathVariable int id) {
        return myUserService.getByID(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody Map<String, Object> userData, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyUser user;
            if (tableName.equals("user_donor")) {
                user = new UserDonor();
            } else if (tableName.equals("user_recipient")) {
                user = new UserRecipient();
                ((UserRecipient) user).setOrganizationID((int) userData.get("organization_id"));
            } else {
                response.put("success", false);
                response.put("message", "Invalid table name");
                return ResponseEntity.badRequest().body(response);
            }
    
            user.setUserName((String) userData.get("user_name"));
            user.setPassword((String) userData.get("password"));
            user.setEmail((String) userData.get("email"));
            user.setPhoneNumber((String) userData.get("phone_number"));
            user.setUserStatusID(1);
    
            myUserService.save(user, tableName);
    
            response.put("success", true);
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error registering user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    

    @PutMapping("/{tableName}/update/profile/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody Map<String, Object> userData, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyUser user;
            if (tableName.equals("user_donor")) {
                user = new UserDonor();
            } else if (tableName.equals("user_recipient")) {
                user = new UserRecipient();
            } else {
                response.put("success", false);
                response.put("message", "Invalid table name");
                return ResponseEntity.badRequest().body(response);
            }
    
            user.setUserName((String) userData.get("user_name"));
            user.setEmail((String) userData.get("email"));
            user.setPhoneNumber((String) userData.get("phone_number"));
    
            MyUser updatedUser = myUserService.updateProfile(user, id, tableName);
    
            response.put("success", true);
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{tableName}/update/password/{id}")
    public ResponseEntity<Map<String, Object>> updatePass( @RequestBody Map<String, Object> userData, @PathVariable String tableName, @PathVariable int id) {    
        Map<String, Object> response = new HashMap<>();
        try {
            String newPassword = (String) userData.get("password");
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }

            MyUser updatedUser = myUserService.updatePass(newPassword, id, tableName);
            
            response.put("success", true);
            response.put("message", "Password updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/{tableName}/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try{
            myUserService.delete(id, tableName);
            response.put("success", true);
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e){ 
            response.put("success", false);
            response.put("message", "Error deleting user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email){
        boolean exist = myUserService.checkEmailExists(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exist);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tableName}/login")
    public ResponseEntity<?> login(@PathVariable String tableName, @RequestBody LoginRequest request) {
        MyUser user = myUserService.login(tableName, request.getEmail(), request.getPassword());
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        
        String userType = tableName.equals("user_donor") ? "donor" : " recipient";
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("userType", userType);
        return ResponseEntity.ok(response);
    }
}