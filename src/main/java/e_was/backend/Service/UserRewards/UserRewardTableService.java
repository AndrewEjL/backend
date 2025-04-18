package e_was.backend.Service.UserRewards;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.UserRewards.MyUserRewards;

@Service
public class UserRewardTableService {
    private final Map<String, Class<? extends MyUserRewards>> tableNames;

    public UserRewardTableService(){
        tableNames = new HashMap<>();
        tableNames.put("user_donor_rewards", MyUserRewards.class);
    }

    public Class<? extends MyUserRewards> getEntity(String tableName){
        return tableNames.get(tableName);
    }
}
