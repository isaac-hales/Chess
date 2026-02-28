package server;

import chess.AuthData;
import chess.ChessGame;
import chess.UserData;
import io.javalin.*;

import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    //Storage for the users logging in, and the authTokens
    private final Map<String, UserData> users = new HashMap<>(); //Key is userName,
    private final Map<String, AuthData> authTokens = new HashMap<>(); //Key is userName
    private final Map<String, ChessGame> allChessGames = new HashMap<>(); //Key is GameID

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.

        //CLEAR APPLICATION
        javalin.delete("/db",ctx -> {
            //Clearing out all items / storages
            removeAllItems(users);
            removeAllItems(authTokens);
            removeAllItems(allChessGames);
            ctx.status(200);
            ctx.result("{}");
        });

        //Register
        javalin.post("/user", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            String username = user.userName();
            String password = user.userPassword();
            String email = user.userEmail();

            try {
                if (isInvalid(username) || isInvalid(password) || isInvalid(email)) {
                    ctx.status(400);
                    ctx.result("{ \"message\": \"Error: bad request\" }");
                }
                //If the username was NOT found in the users Map
                else if (findUserName(username) != null) {
                    ctx.status(403); //Client Error, cause the client tried an already taken username.
                    ctx.result("{ \"message\": \"Error: already taken\" }");
                }
                //If the username WAS found in the users Map
                else {
                    //Return 200,
                    ctx.status(200);
                    AuthData authUser = AuthData.create(username);
                    //Returning the username and the authToken
                    ctx.json(authUser);
                    users.put(user.userName(), user);
                    authTokens.put(authUser.authToken(), authUser);
                }
            }
            //If there was any other sort of issue with connecting to the server
            catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            }

        });

        //Login
        javalin.post("/session", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            String username = user.userName();
            String password = user.userPassword();


            try {
                if (isInvalid(username) || isInvalid(password)) {
                    ctx.status(400);
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
                //Checking if the username is correct
                else if (findUserName(username) == null) {
                    ctx.status(401); //Client Error, cause the client tried an already taken username.
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
                //Checking if the password is wrong.
                else if (!password.equals(users.get(username).userPassword())) {
                    ctx.status(401); //Client Error, cause the client tried an incorrect password
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
                //If the username WAS found in the users Map
                else {
                    //Return 200,
                    ctx.status(200);
                    AuthData authUser = AuthData.create(username);
                    //Returning the username and the authToken
                    ctx.json(authUser);
                    authTokens.put(authUser.authToken(), authUser);
                }
            }
            //If there was any other sort of issue with connecting to the server
            catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            }
        });


        //Logout
        //How do I do this then without duplication?
        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            try {
                //private final Map<String, AuthData> authTokens = new HashMap<>();
                if (authTokens.containsKey(authToken)) {
                    authTokens.remove(authToken);
                    ctx.status(200);
                    ctx.result("{}");
                }
                else {
                    ctx.status(401);
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
            }
            catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            }
        });


        //List Games
        /**
         * Gives a list of all games.
         */


        //Create Game
        /**
         * Creates a new game
         */


        //Join Game
        /**
         * Verifies that the specified game exists and adds the
         * caller as the requested color to the game.
         */
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private String findUserName(String theUserName) {
        if (users.containsKey(theUserName)) { return theUserName; }
        return null;
    }

    //This will return a true if the item is null or blank spaces, which should cause an error. Else it will return false
    private boolean isInvalid(String item){
        return item == null || item.isBlank();
    }


    //Maybe think about removing this. It can be implemented into the code pretty easily.
    private void removeAllItems(Map givenMap){
        givenMap.clear();
    }

    public void stop() {
        javalin.stop();
    }
}
