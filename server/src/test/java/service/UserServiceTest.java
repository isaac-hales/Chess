package service;


import chess.AuthData;
import chess.UserData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    // These will run before/after each test, so that we have a clean slate
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;
    private ClearService clearService;

    @BeforeEach  // runs before EVERY test
    void setup() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(userDAO, new GameDAO(), authDAO);
        try {
            clearService.clear(); //Makes sure that everything is nice and new
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerSuccess() {
        //Setting up the test data
        UserData user = new UserData("testUser", "password", "email@test.com");
        //Actually running the register
        AuthData result = userService.register(user);
        //Checking that the result works with assert
        assertNotNull(result);
        assertEquals("testUser", result.userName());
    }

    @Test
    void registerFailAlreadyTaken() {
        //This is use setting it up
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);
        //registering same user twice should throw exception, because hte username has already been used
        assertThrows(ServiceException.class, () -> userService.register(user));
    }

    @Test
    void loginSuccess() {
        //Getting the expected user & running the performance on them
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);
        AuthData result = userService.login(user);
        //Asserting that this is accurate.
        assertNotNull(result);
        assertEquals("testUser", user.userName());
    }

    @Test
    void loginFailNotRegistered() {
        UserData user = new UserData("testUser", "password", "email@test.com");
        assertThrows(ServiceException.class, () -> userService.login(user));
    }

    @Test
    void loginFailWrongPassword(){
        //Getting the user logged in
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);
        //Making a false user data
        UserData wrongUser = new UserData("testUser", "wrongPassword", "email@test.com");
        assertThrows(ServiceException.class, () -> userService.login(wrongUser));
    }

    @Test
    void logoutSuccess(){
        UserData user = new UserData("testUser", "password", "email@test.com");
        userService.register(user);
        AuthData tempData = userService.login(user);
        assertDoesNotThrow(() -> userService.logout(tempData.authToken()));
    }

    @Test
    void logoutFailNoUser(){
        assertThrows(ServiceException.class, () -> userService.logout(null));
    }

}
