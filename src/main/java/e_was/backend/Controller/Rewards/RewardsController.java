package e_was.backend.Controller.Rewards;

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

import e_was.backend.Model.Rewards.MyRewards;
import e_was.backend.Service.Rewards.MyRewardsService;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
    @Autowired
    private MyRewardsService myRewardsService;
    
    @GetMapping("/{tableName}/all")
    public List<? extends MyRewards> getAll(@PathVariable String tableName) {
        return myRewardsService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public List<MyRewards> getByID(@PathVariable String tableName, @PathVariable int id) {
        return myRewardsService.getByID(id, tableName);
    }

    @GetMapping("/{tableName}/type/{id}")
    public List<MyRewards> getByRewardsID(@PathVariable String tableName, @PathVariable int id) {
        return myRewardsService.getByRewardsID(id, tableName);
    }

    @GetMapping("/{tableName}/points/{id}")
    public List<MyRewards> getByRewardsPoints(@PathVariable String tableName, @PathVariable int minPoint, @PathVariable int maxPoints) {
        return myRewardsService.getByRewardsPoints(minPoint, maxPoints, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody MyRewards myRewards, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyRewards addRewards = myRewardsService.save(myRewards, tableName);

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
    public ResponseEntity<Map<String, Object>> updateItem(@RequestBody MyRewards myRewards, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyRewards updatedRewards = myRewardsService.update(myRewards, id, tableName);
    
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

    @PutMapping("/{tableName}/update/quantity/{id}")
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestBody MyRewards myRewards, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyRewards updatedRewards = myRewardsService.updateQuantity(myRewards, id, tableName);
    
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
            myRewardsService.delete(id, tableName);
    
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
