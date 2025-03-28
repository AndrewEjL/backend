package e_was.backend.Service.PickupTransaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.PickupTransaction.MyTransaction;

@Service
public class TransactionTable {
    private final Map<String, Class<? extends MyTransaction>> tableNames;

    public TransactionTable(){
        tableNames = new HashMap<>();
        tableNames.put("pickup_transaction", MyTransaction.class);
    }

    public Class<? extends MyTransaction> getEntity(String tableName) {
        Class<? extends MyTransaction> entity = tableNames.get(tableName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return entity;
    }
}
