package service;

import dataaccess.*;

public class ClearService {

    private final UserDAOInterface userDAO;
    private final GameDAOInterface gameDAO;
    private final AuthDAOInterface authDAO;

    public ClearService(UserDAOInterface userDAO, GameDAOInterface gameDAO, AuthDAOInterface authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
