package e_was.backend.Service.Organization;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.Organization.MyOrganizationStat;

@Service
public class OrganizationStatsTable {
    private final Map<String, Class<? extends MyOrganizationStat>> tableNames;

    public OrganizationStatsTable(){
        tableNames = new HashMap<>();
        tableNames.put("organization_stats", MyOrganizationStat.class);
    }

    public Class<? extends MyOrganizationStat> getEntity(String tableName) {
        Class<? extends MyOrganizationStat> entity = tableNames.get(tableName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return entity;
    }
    
}
