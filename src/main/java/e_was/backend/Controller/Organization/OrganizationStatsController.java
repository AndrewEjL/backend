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
import e_was.backend.Model.Organization.MyOrganizationStat;
import e_was.backend.Model.User.MyUser;
import e_was.backend.Service.Organization.MyOrganizationStatsService;

@RestController
@RequestMapping("/api/organizationStats")
public class OrganizationStatsController {
    @Autowired
    private MyOrganizationStatsService myOrganizationStatsService;

    @GetMapping("/{tableName}/all")
    public List<? extends MyOrganizationStat> getAll(@PathVariable String tableName) {
        return myOrganizationStatsService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public MyOrganizationStat getByID(@PathVariable String tableName, @PathVariable int id) {
        return myOrganizationStatsService.getByID(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addOrganization(@RequestBody MyOrganizationStat organization, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyOrganizationStat savedOrganization = myOrganizationStatsService.save(organization, tableName);
    
            response.put("success", true);
            response.put("message", "Organization stats added successfully");
            response.put("data", savedOrganization);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/{tableName}/update/{id}")
    public ResponseEntity<Map<String, Object>> updateOrganization(@RequestBody MyOrganizationStat organization, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyOrganizationStat updatedOrganization = myOrganizationStatsService.update(organization, id, tableName);
    
            response.put("success", true);
            response.put("message", "Organization stats updated successfully");
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
            myOrganizationStatsService.delete(id, tableName);
    
            response.put("success", true);
            response.put("message", "Organization deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
