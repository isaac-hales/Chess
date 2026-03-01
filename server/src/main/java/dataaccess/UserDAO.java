package dataaccess;


import chess.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private final Map<String, UserData> users = new HashMap<>(); //Key is userName,

    public void createUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.userName())){
            throw new DataAccessException("User already exists");
        }
        else {
            users.put(user.userName(),user);
        }
    }

    public UserData getUser(String username) {
        //Will send up a null if it doesn't work
        return users.get(username);
    }

    public void clear() throws DataAccessException {
        users.clear();
    }

}
