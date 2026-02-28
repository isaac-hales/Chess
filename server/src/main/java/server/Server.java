package server;

import chess.AuthData;
import chess.ChessGame;
import chess.GameData;
import chess.UserData;
import io.javalin.*;
import java.util.Random;


import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    //Storage for the users logging in, and the authTokens
    private final Map<String, UserData> users = new HashMap<>(); //Key is userName,
    private final Map<String, AuthData> authTokens = new HashMap<>(); //Key is userName
    private final Map<Integer, GameData> allChessGames = new HashMap<>(); //authToken?

    //Think about making an authorization helper function
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
        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");

            try {
                //private final Map<String, AuthData> authTokens = new HashMap<>();
                if (authTokens.containsKey(authToken)) {
                    ctx.status(200);
                    //I'm not sure how, but I need to access all of this from the GameMap, I think, but we'll worry about it later.
                    ctx.json(Map.of("games",allChessGames.values()));
                    //ctx.result("{ \"games\": [{\"gameID\": 1234, \"whiteUsername\":\"\", \"blackUsername\":\"\", \"gameName:\"\"} ]}");
                }
                else {
                    ctx.status(401);
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
            }
            catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: (description of error)\" }");
            }
        });


        //Create Game
        javalin.post("/game", ctx -> {
            try {
                //private final Map<String, AuthData> authTokens = new HashMap<>();
                String authToken = ctx.header("authorization");
                if (authTokens.containsKey(authToken)) {
                    ctx.status(200);
                    var body = ctx.bodyAsClass(Map.class);
                    String gameName = (String) body.get("gameName");
                    int gameID = allChessGames.size() + 1;
                    GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
                    allChessGames.put(gameID, newGame);
                    ctx.json(Map.of("gameID", gameID));
                }
                else {
                    ctx.status(401);
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
            }
            catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: (description of error)\" }");
            }
        });


        //Join Game
        javalin.put("/game",ctx -> {
            try {
                String authToken = ctx.header("authorization");
                var body = ctx.bodyAsClass(Map.class);
                //get the gameID & team color
                int currentGameID = (Integer) body.get("gameID");
                String playerColor = (String) body.get("playerColor");
                GameData updatedGame;
                if (!isAuthorized(authToken)) {
                    ctx.status(401);
                    ctx.result("{ \"message\": \"Error: unauthorized\" }");
                }
                else if (!allChessGames.containsKey(currentGameID)) {
                    ctx.status(400);
                    ctx.result("{ \"message\": \"Error: bad request\" }");
                }
                else {
                    //need to get gameID and use that to find the game, and then add the playerColor, then update the game
                    String username = authTokens.get(authToken).userName();
                    GameData currentGame = allChessGames.get(currentGameID);
                    //If the player joining is white
                    if (playerColor.equalsIgnoreCase("WHITE") && currentGame.whiteUsername() == null){
                        updatedGame = new GameData(currentGameID, username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
                    }
                    //if the player joining is black
                    else if (playerColor.equalsIgnoreCase("BLACK") && currentGame.blackUsername() == null){
                        updatedGame = new GameData(currentGameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game());
                    }
                    else {
                        ctx.status(403);
                        ctx.result("{ \"message\": \"Error: already taken\" }");
                        return;
                    }
                    allChessGames.put(currentGameID, updatedGame);
                    ctx.status(200);
                    ctx.result("{}");

                }

            }
            catch (Exception e){
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: (description of error)\" }");
            }
        });
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
    private void removeAllItems(Map givenMap) {
        givenMap.clear();
    }

    //A helper function for checking authorization. It checks that the authToken isn't null, and that it's listed in the database.
    private boolean isAuthorized(String authToken){
        return authToken != null && authTokens.containsKey(authToken);
    }

    public void stop() {
        javalin.stop();
    }
}
