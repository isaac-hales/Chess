package service;

import chess.AuthData;
import chess.GameData;
import chess.UserData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    // These will run before/after each test, so that we have a clean slate
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;
    private ClearService clearService;
    private GameService gameService;

    @BeforeEach
        // runs before EVERY test
    void setup() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        clearService = new ClearService(userDAO, new GameDAO(), authDAO);
        try {
            clearService.clear(); //Makes sure that everything is nice and new
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGamesSuccess() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        int testGameID = gameService.createGame("test Game",tempData.authToken());
        //Compares the list size to the testGameID. This way it isn't hardcoded and hte test can be run multiple times.
        assertEquals(gameService.listGames(tempData.authToken()).size(), testGameID);
    }

    @Test
    void createGamesFailure() {
        //Cannot create game without game name or auth token.
        assertThrows(ServiceException.class, () -> gameService.createGame(null, null));
    }

    @Test
    void listGamesSuccess() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        gameService.createGame("test Game",tempData.authToken());
        Collection<GameData> result = gameService.listGames(tempData.authToken());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void listGamesFailure() {
        //No user can be found / user does not exist
        assertThrows(ServiceException.class, () -> gameService.listGames(null));
    }

    @Test
    void joinGameSuccess() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        int testGameID = gameService.createGame("test Game",tempData.authToken());
        assertDoesNotThrow(() -> gameService.joinGame(tempData.authToken(),testGameID,"White" ));
        assertDoesNotThrow(() -> gameService.joinGame(tempData.authToken(),testGameID,"Black" ));
    }

    @Test
    void joinGameFailSameColor() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        int testGameID = gameService.createGame("test Game",tempData.authToken());
        gameService.joinGame(tempData.authToken(),testGameID,"White" );
        assertThrows(ServiceException.class, () -> gameService.joinGame(tempData.authToken(),testGameID,"White" ));
    }

    @Test
    void joinGameFailWrongID() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        AuthData tempData = userService.register(user);
        gameService.createGame("test Game",tempData.authToken());
        assertThrows(ServiceException.class, () -> gameService.joinGame(tempData.authToken(),0,"White" ));
    }
}
