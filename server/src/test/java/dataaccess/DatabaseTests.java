package dataaccess;

import chess.AuthData;
import chess.GameData;
import chess.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    private UserDAOInterface userDAO;
    private AuthDAOInterface authDAO;
    private GameDAOInterface gameDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    // -------------------- UserDAO Tests --------------------

    @Test
    void createUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userDAO.createUser(user);
        UserData result = userDAO.getUser("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.username());
    }

    @Test
    void createUserFailDuplicate() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userDAO.createUser(user);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserData user = new UserData("testUser", "password", "email@test.com");
        userDAO.createUser(user);
        UserData result = userDAO.getUser("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.username());
        assertEquals("email@test.com", result.email());
    }

    @Test
    void getUserNotFound() throws DataAccessException {
        UserData result = userDAO.getUser("nonexistent");
        assertNull(result);
    }

    @Test
    void clearUsersSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "password", "email@test.com"));
        userDAO.clear();
        assertNull(userDAO.getUser("testUser"));
    }

    // -------------------- AuthDAO Tests --------------------

    @Test
    void createAuthSuccess() throws DataAccessException {
        AuthData result = authDAO.createAuth("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    void createAuthFailNullUser() {
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUser");
        AuthData result = authDAO.getAuth(auth.authToken());
        assertNotNull(result);
        assertEquals("testUser", result.username());
    }

    @Test
    void getAuthNotFound() throws DataAccessException {
        AuthData result = authDAO.getAuth("fakeToken");
        assertNull(result);
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUser");
        authDAO.deleteAuth(auth.authToken());
        assertNull(authDAO.getAuth(auth.authToken()));
    }

    @Test
    void deleteAuthNonexistent() {
        // Deleting a token that doesn't exist should not throw
        assertDoesNotThrow(() -> authDAO.deleteAuth("nonexistentToken"));
    }

    @Test
    void clearAuthSuccess() throws DataAccessException {
        authDAO.createAuth("testUser");
        authDAO.clear();
        assertNull(authDAO.getAuth("anyToken"));
    }

    // -------------------- GameDAO Tests --------------------

    @Test
    void createGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "testGame", null);
        int gameID = gameDAO.createGame(game);
        assertTrue(gameID > 0);
    }

    @Test
    void createGameFailNullName() {
        GameData game = new GameData(0, null, null, null, null);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "testGame", null);
        int gameID = gameDAO.createGame(game);
        GameData result = gameDAO.getGame(gameID);
        assertNotNull(result);
        assertEquals("testGame", result.gameName());
    }

    @Test
    void getGameNotFound() throws DataAccessException {
        GameData result = gameDAO.getGame(99999);
        assertNull(result);
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "game1", null));
        gameDAO.createGame(new GameData(0, null, null, "game2", null));
        assertEquals(2, gameDAO.listGames().size());
    }

    @Test
    void listGamesEmpty() throws DataAccessException {
        assertEquals(0, gameDAO.listGames().size());
    }

    @Test
    void updateGameSuccess() throws DataAccessException {
        GameData game = new GameData(0, null, null, "testGame", null);
        int gameID = gameDAO.createGame(game);
        GameData updated = new GameData(gameID, "whitePlayer", null, "testGame", null);
        gameDAO.updateGame(updated);
        GameData result = gameDAO.getGame(gameID);
        assertEquals("whitePlayer", result.whiteUsername());
    }

    @Test
    void updateGameNotFound() {
        GameData ghost = new GameData(99999, "whitePlayer", null, "ghost", null);
        // Should not throw, but nothing should be updated
        assertDoesNotThrow(() -> gameDAO.updateGame(ghost));
    }

    @Test
    void clearGamesSuccess() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "testGame", null));
        gameDAO.clear();
        assertEquals(0, gameDAO.listGames().size());
    }
}
