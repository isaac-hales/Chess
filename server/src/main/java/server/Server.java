package server;

import chess.AuthData;
import chess.UserData;
import io.javalin.*;

import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    //Storage for the users logging in, and the authTokens
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.

        //CLEAR APPLICATION
        /**
         * Clears the database. Removes all users, games, and authTokens
         */
        javalin.delete("/user",ctx -> {

        });


        //Register
        /**
         * Register a new user.
         */
        //URL Path /db, HTTP Method: DELETE, Success: [200] {}, Failure: [500] {"message": "Error: (description of error)" }

        //Double Check that User is the correct name / approach
        javalin.post("/user", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            String username = user.userName();

            //If the username was NOT found in the users Map
            if (findUserName(username) == null) {
                //Return 200,
                ctx.status(200);
                AuthData authUser = AuthData.create(username);
                //
                ctx.json(authUser);
                users.put(user.userName(), user);
                authTokens.put(authUser.authToken(), authUser);
            }
            //If the username WAS found in the users Map
            else {
                ctx.status(403); //Client Error, cause the client tried an already taken username.
                ctx.result("message : Error: (description of error)");
            }

        });


        //Login
        /**
         * Logs in an existing user (returns a new authToken)
         */


        //Logout
        /**
         * Logs out the user represented by the authToken
         */


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

    //Can we use this for finding an authTokens too? I think so.
    private String findUserName(String theUserName) {
        if (users.containsKey(theUserName)) { return theUserName; }
        return null;
    }

    private void removeAllItems(Map givenMap){
        givenMap.clear();
    }

    public void stop() {
        javalin.stop();
    }
}
