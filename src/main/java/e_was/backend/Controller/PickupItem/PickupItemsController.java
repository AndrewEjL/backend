package e_was.backend.Controller.PickupItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_was.backend.DTO.PickupItemDTO;
import e_was.backend.Model.Organization.MyOrganization;
import e_was.backend.Model.PickupItem.MyPickupItem;
import e_was.backend.Service.PickupItem.MyPickupItemService;

@RestController
@RequestMapping("/api/item")
public class PickupItemsController {
    @Autowired
    private MyPickupItemService myPickupItemService;

    @GetMapping("/{tableName}/all")
    public List<? extends MyPickupItem> getAll(@PathVariable String tableName) {
        return myPickupItemService.getAll(tableName);
    }

    @GetMapping("/{tableName}/allWStatus")
    public List<? extends MyPickupItem> getAllWithoutStatus(@PathVariable String tableName) {
        return myPickupItemService.getAllWithoutID(tableName);
    }

    @GetMapping("/{tableName}/{id}")
    public List<MyPickupItem> getByID(@PathVariable String tableName, @PathVariable int id) {
        return myPickupItemService.getByID(id, tableName);
    }

    // display by item id
    @GetMapping("/{tableName}/get/{id}")
    public Optional<MyPickupItem> getByItemID(@PathVariable String tableName, @PathVariable int id) {
        return myPickupItemService.getByItemID(id, tableName);
    }
    
    // display by item id
    @GetMapping("/{tableName}/gets/{id}")
    public List<MyPickupItem> getByItemsID(@PathVariable String tableName, @PathVariable int id) {
        return myPickupItemService.getByItemsID(id, tableName);
    }

    // display by item id
    @GetMapping("/{tableName}/getHistory/{id}")
    public List<MyPickupItem> getHistoryItems(@PathVariable String tableName, @PathVariable int id) {
        return myPickupItemService.getHistoryItem(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody MyPickupItem pickupItem, @PathVariable  String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyPickupItem saveItem = myPickupItemService.save(pickupItem, tableName);

            response.put("success", true);
            response.put("message", "Pickup Item(s) added successfully");
            response.put("data", saveItem);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/{tableName}/update/{id}/{itemName}")
    public ResponseEntity<Map<String, Object>> updateItem(@RequestBody MyPickupItem item, @PathVariable int id, @PathVariable String itemName, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            int updatedItem = myPickupItemService.update(item, id, tableName, itemName);
    
            if (updatedItem > 0) {
                response.put("success", true);
                response.put("message", "Updated " + updatedItem + " items successfully.");
            } else {
                response.put("success", false);
                response.put("message", "No items updated. Check if items exist or meet update conditions.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

        
    @PutMapping("/{tableName}/update/status/{id}")
    public ResponseEntity<Map<String, Object>> updateItemStatus(@RequestBody MyPickupItem item, @PathVariable int id, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            int updatedItem = myPickupItemService.updateStatus(item, id, tableName);
    
            if (updatedItem > 0) {
                response.put("success", true);
                response.put("message", "Updated " + updatedItem + " items successfully.");
            } else {
                response.put("success", false);
                response.put("message", "No items updated. Check if items exist or meet update conditions.");
            }
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
            myPickupItemService.delete(id, tableName);
    
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
