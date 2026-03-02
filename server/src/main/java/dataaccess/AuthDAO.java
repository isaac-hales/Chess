package dataaccess;

import chess.AuthData;
import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>(); //Key is authToken

    public AuthData createAuth(String username) throws DataAccessException{
        AuthData newAuth = AuthData.create(username);
        authTokens.put(newAuth.authToken(),newAuth);
        return newAuth;
    }

    public AuthData getAuth(String authToken){
        return authTokens.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        if (!authTokens.containsKey(authToken)){
            throw new DataAccessException("Auth token does not exist");
        }
        authTokens.remove(authToken);
    }

    public void clear(){
        authTokens.clear();
    }

}
