package e_was.backend.Controller.Organization;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import e_was.backend.Service.Organization.OrganizationTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_was.backend.DTO.LoginRequest;
import e_was.backend.Model.Organization.MyOrganization;
import e_was.backend.Service.Organization.MyOrganizationService;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
    @Autowired
    private MyOrganizationService myOrganizationService;

    @GetMapping("/{tableName}/all")
    public List<? extends MyOrganization> getAll(@PathVariable String tableName) {
        return myOrganizationService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public MyOrganization getByID(@PathVariable String tableName, @PathVariable int id) {
        return myOrganizationService.getByID(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addOrganization(@RequestBody MyOrganization organization, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyOrganization savedOrganization = myOrganizationService.save(organization, tableName);
    
            response.put("success", true);
            response.put("message", "Organization added successfully");
            response.put("data", savedOrganization);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/{tableName}/update/{id}")
    public ResponseEntity<Map<String, Object>> updateOrganization(@RequestBody MyOrganization organization, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyOrganization updatedOrganization = myOrganizationService.update(organization, id, tableName);
    
            response.put("success", true);
            response.put("message", "Organization updated successfully");
            response.put("data", updatedOrganization);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/{tableName}/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrganization(@PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            myOrganizationService.delete(id, tableName);
    
            response.put("success", true);
            response.put("message", "Organization deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email){
        boolean exist = myOrganizationService.checkEmailExists(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exist);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tableName}/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        MyOrganization organization = myOrganizationService.login(request.getEmail(), request.getPassword());

        if(organization == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("id", organization.getOrganizationID());
        response.put("userType", "organization");
    
        return ResponseEntity.ok(response);
    }
    
}
