package e_was.backend.Controller.PickupTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_was.backend.Model.PickupItem.MyPickupItem;
import e_was.backend.Model.PickupTransaction.MyTransaction;
import e_was.backend.Service.PickupTransaction.MyTransactionService;

@RestController
@RequestMapping("/api/transaction")
public class PickupTransactionController {
    @Autowired
    private MyTransactionService myTransactionService;

    @GetMapping("/{tableName}/all")
    public List<? extends MyTransaction> getAll(@PathVariable String tableName) {
        return myTransactionService.getAll(tableName);
    }

    @GetMapping("/{tableName}/{id}/{orgID}")
    public List<MyTransaction> getByID(@PathVariable String tableName, @PathVariable int id, @PathVariable int orgID) {
        return myTransactionService.getByID(id, orgID, tableName);
    }

    @GetMapping("/{tableName}/distinctOrg/{id}")
    public List<MyTransaction> getDistinctOrgIDs(@PathVariable String tableName, @PathVariable int id) {
        return myTransactionService.getDistinctOrgIDsByDonorID(id, tableName);
    }

    @GetMapping("/{tableName}/history/{id}")
    public List<MyTransaction> getHistory(@PathVariable String tableName, @PathVariable int id) {
        return myTransactionService.getHistory(id, tableName);
    }

    @PostMapping("/{tableName}/add")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody MyTransaction transaction, @PathVariable String tableName) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyTransaction addTransaction = myTransactionService.save(transaction, tableName);

            response.put("success", true);
            response.put("message", "Pickup Item(s) added successfully");
            response.put("data", addTransaction);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding item: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/{tableName}/update/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@RequestBody MyTransaction transaction, @PathVariable String tableName, @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MyTransaction updatedTransaction = myTransactionService.update(transaction, id, tableName);
    
            response.put("success", true);
            response.put("message", "Item updated successfully");
            response.put("data", updatedTransaction);
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
            myTransactionService.delete(id, tableName);
    
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
