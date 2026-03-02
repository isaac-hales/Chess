package service;
import chess.AuthData;
import chess.UserData;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) {
        try {
            if (userDAO.getUser(user.userName()) != null) {
                throw new ServiceException(403, "Error: already taken");
            }
            userDAO.createUser(user);
            return authDAO.createAuth(user.userName());
        }
        catch (DataAccessException e){
            throw new ServiceException(500, "Error: " + e.getMessage());
        }
    }

    public AuthData login(UserData user) {
        try {
            UserData existingUser = userDAO.getUser(user.userName());
            if (existingUser == null) {
                throw new ServiceException(401, "Error: User does not exist");
            }
            if (!user.userPassword().equals(existingUser.userPassword())) {
                throw new ServiceException(401, "Error: Wrong Password");
            }
            return authDAO.createAuth(user.userName());
        }
        catch (DataAccessException e) {
            throw new ServiceException(500, "message" + e.getMessage());
        }
    }

    public void logout(String authToken)  {
        try {
            if (authDAO.getAuth(authToken) == null) {
                throw new ServiceException(401, "Error: User does not exist");
            }
            authDAO.deleteAuth(authToken);
        }
        catch (DataAccessException e) {
            throw new ServiceException(500, "message" + e.getMessage());
        }
    }

}
