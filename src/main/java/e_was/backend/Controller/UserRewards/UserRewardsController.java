package e_was.backend.Controller.UserRewards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_was.backend.Model.UserRewards.MyUserRewards;
import e_was.backend.Service.UserRewards.MyUserRewardsService;

@RestController
@RequestMapping("/api/userRewards")
public class UserRewardsController {
    @Autowired
    private MyUserRewardsService myUserRewardsService;
    
    @GetMapping("/{tableName}/all")
    public List<? extends MyUserRewards> getAll(@PathVariable String tableName) {
        return myUserRewardsService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public List<MyUserRewards> getByID(@PathVariable String tableName, @PathVariable int id) {
        return myUserRewardsService.getByID(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody MyUserRewards myUserRewards, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyUserRewards addRewards = myUserRewardsService.save(myUserRewards, tableName);

            response.put("success", true);
            response.put("message", "Rewards(s) added successfully");
            response.put("data", addRewards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/{tableName}/update/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@RequestBody MyUserRewards myUserRewards, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyUserRewards updatedRewards = myUserRewardsService.update(myUserRewards, id, tableName);
    
            response.put("success", true);
            response.put("message", "Item updated successfully");
            response.put("data", updatedRewards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/{tableName}/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            myUserRewardsService.delete(id, tableName);
    
            response.put("success", true);
            response.put("message", "Item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting Item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
