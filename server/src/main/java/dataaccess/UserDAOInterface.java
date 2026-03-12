package dataaccess;

import chess.UserData;

public interface UserDAOInterface {
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clearUser() throws DataAccessException;
}