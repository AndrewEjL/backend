package e_was.backend.Service.Rewards;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.Rewards.MyRewards;

@Service
public class RewardTableService {
    private final Map<String, Class<? extends MyRewards>> tableNames;

    public RewardTableService(){
        tableNames = new HashMap<>();
        tableNames.put("rewards", MyRewards.class);
    }

    public Class<? extends MyRewards> getEntity(String tableName){
        return tableNames.get(tableName);
    }
}
