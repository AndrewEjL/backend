package e_was.backend.Service.PickupItem;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.PickupItem.MyPickupItem;


@Service
public class ItemTable {
    private final Map<String, Class<? extends MyPickupItem>> tableNames;

    public ItemTable(){
        tableNames = new HashMap<>();
        tableNames.put("pickup_items", MyPickupItem.class);
    }

    public Class<? extends MyPickupItem> getEntity(String tableName) {
        Class<? extends MyPickupItem> entity = tableNames.get(tableName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return entity;
    }
}
