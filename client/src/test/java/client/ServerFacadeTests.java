package client;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailDoubleRegister() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> facade.register("player1", "password", "p1@email.com"));
    }

    @Test
    void registerFailNullUsername() {
        assertThrows(Exception.class, () -> facade.register(null, "password", "p1@email.com"));
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> facade.login("player1", "WRONGPASSWORD"));
    }

    @Test
    void logoutSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        facade.logout(authData.authToken());
        assertThrows(Exception.class, () -> facade.createGame("GameName", authData.authToken()));
    }

    @Test
    void logoutFail() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
       assertThrows(Exception.class, () -> facade.logout("fakeAuthToken"));
    }

    

}
