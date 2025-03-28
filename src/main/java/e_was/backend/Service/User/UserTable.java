package e_was.backend.Service.User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import e_was.backend.Model.User.MyUser;
import e_was.backend.Model.User.UserDonor;
import e_was.backend.Model.User.UserRecipient;

@Service
public class UserTable {
    private final Map<String, Class<? extends MyUser>> tableNames;

    public UserTable(){
        tableNames = new HashMap<>();
        tableNames.put("user_donor", UserDonor.class);
        tableNames.put("user_recipient", UserRecipient.class);
    }

    public Class<? extends MyUser> getEntity(String tableName){
        return tableNames.get(tableName);
    }
}
