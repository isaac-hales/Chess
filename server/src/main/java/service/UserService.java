package service;
import chess.AuthData;
import chess.UserData;
import dataaccess.*;

public class UserService {

    private final UserDAOInterface userDAO;
    private final AuthDAOInterface authDAO;

    public UserService(UserDAOInterface userDAO, AuthDAOInterface authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) {
        try {
            if (userDAO.getUser(user.username()) != null) {
                throw new ServiceException(403, "Error: already taken");
            }
            // Hash password before storing
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(user.password(), org.mindrot.jbcrypt.BCrypt.gensalt());
            UserData hashedUser = new UserData(user.username(), hashedPassword, user.email());
            userDAO.createUser(hashedUser);
            return authDAO.createAuth(user.username());
        }
        catch (DataAccessException e){
            System.out.println("register failed: " + e.getMessage());
            throw new ServiceException(500, "Error: " + e.getMessage());
        }
    }

    public AuthData login(UserData user) {
        try {
            UserData existingUser = userDAO.getUser(user.username());
            if (existingUser == null) {
                throw new ServiceException(401, "Error: unauthorized");
            }
            try {
                if (!org.mindrot.jbcrypt.BCrypt.checkpw(user.password(), existingUser.password())) {
                    throw new ServiceException(401, "Error: unauthorized");
                }
            } catch (IllegalArgumentException e) {
                // Invalid password format (null or malformed hash)
                throw new ServiceException(401, "Error: unauthorized");
            }
            return authDAO.createAuth(user.username());
        }
        catch (DataAccessException e) {
            throw new ServiceException(500, "Error: " + e.getMessage());
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
            System.out.println("register failed: " + e.getMessage());
            throw new ServiceException(500, "Error: " + e.getMessage());
        }
    }

}
