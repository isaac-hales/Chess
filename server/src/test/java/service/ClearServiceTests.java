package service;

import chess.AuthData;
import chess.UserData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClearServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO = new GameDAO();
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    @BeforeEach
        // runs before EVERY test
    void setup() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);
        try {
            clearService.clear(); //Makes sure that everything is nice and new
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        //This is so that we have stuff to delete / try to delete
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        gameService.createGame("test Game",tempData.authToken());
    }

    @Test
    void clearSuccess() throws Exception{
        clearService.clear();
        AuthData tempData = userService.register(new UserData("newUser","password", "email@test.com"));
        assertEquals(0, gameService.listGames(tempData.authToken()).size());
    }

}
