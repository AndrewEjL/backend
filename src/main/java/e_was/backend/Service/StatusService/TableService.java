package e_was.backend.Service.StatusService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.StatusModel.DeviceCondition;
import e_was.backend.Model.StatusModel.ItemStatus;
import e_was.backend.Model.StatusModel.ItemTypes;
import e_was.backend.Model.StatusModel.MyStatus;
import e_was.backend.Model.StatusModel.PickupStatus;
import e_was.backend.Model.StatusModel.RegistrationType;
import e_was.backend.Model.StatusModel.UserStatus;

@Service
public class TableService {
    private final Map<String, Class<? extends MyStatus>> tableNames;

    public TableService(){
        tableNames = new HashMap<>();
        tableNames.put("device_condition", DeviceCondition.class);
        tableNames.put("item_status", ItemStatus.class);
        tableNames.put("item_types", ItemTypes.class);
        tableNames.put("pickup_status", PickupStatus.class);
        tableNames.put("registration_type", RegistrationType.class);
        tableNames.put("user_status", UserStatus.class);
    }

    public Class<? extends MyStatus> getEntity(String tableName){
        return tableNames.get(tableName);
    }

}

