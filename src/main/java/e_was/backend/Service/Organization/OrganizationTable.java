package e_was.backend.Service.Organization;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import e_was.backend.Model.Organization.MyOrganization;

@Service
public class OrganizationTable {
    private final Map<String, Class<? extends MyOrganization>> tableNames;

    public OrganizationTable(){
        tableNames = new HashMap<>();
        tableNames.put("organization", MyOrganization.class);
    }

    public Class<? extends MyOrganization> getEntity(String tableName) {
        Class<? extends MyOrganization> entity = tableNames.get(tableName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return entity;
    }
    
}
