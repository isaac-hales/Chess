package server;

import chess.AuthData;
import chess.GameData;
import chess.UserData;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.ServiceException;

import java.util.Collection;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    //Storage for the users logging in, and the authTokens
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();

    //Giving the DAOs to the Services, like army, navy
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(authDAO, gameDAO);
    private final ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new io.javalin.json.JavalinJackson().updateMapper(mapper -> {
                mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            }));
        });

        javalin.exception(ServiceException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());
            ctx.json(Map.of("message", e.getMessage()));
        });

        //CLEAR APPLICATION
        javalin.delete("/db",ctx -> {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        });

        //Register
        javalin.post("/user", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            if (isInvalid(user.username()) || isInvalid(user.password()) || isInvalid(user.email())) {
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
                return;
            }
            AuthData result = userService.register(user);
            ctx.status(200);
            ctx.json(result);
        });

        //Login
        javalin.post("/session", ctx -> {
            UserData user = ctx.bodyAsClass(UserData.class);
            String username = user.username();
            String password = user.password();
            if (isInvalid(username) || isInvalid(password)) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: unauthorized\" }");
                return;
            }

            AuthData result = userService.login(user);
            ctx.status(200);
            ctx.json(result);
        });

        //Logout
        javalin.delete("/session", ctx -> {
            String authToken = ctx.header("authorization");
            userService.logout(authToken);
            ctx.status(200);
            ctx.result("{}");
        });

        //List Games
        javalin.get("/game", ctx -> {
            String authToken = ctx.header("authorization");
            Collection<GameData> result = gameService.listGames(authToken);
            ctx.status(200);
            ctx.json(Map.of("games",result));
        });

        //Create Game
        javalin.post("/game", ctx -> {
            String authToken = ctx.header("authorization");
            var body = ctx.bodyAsClass(Map.class);
            String gameName = (String) body.get("gameName");
            if (isInvalid(gameName)) {
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
                return;
            }
            int gameID = gameService.createGame(gameName, authToken);
            ctx.status(200);
            ctx.json(Map.of("gameID", gameID));
        });

        //Join Game
        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            var body = ctx.bodyAsClass(Map.class);
            Object tempGameID = body.get("gameID");
            String playerColor = (String) body.get("playerColor");
            if (tempGameID == null) {
                ctx.status(400);
                ctx.json(Map.of("message", "Error: bad request"));
                return;
            }
            int currentGameID = (Integer) tempGameID;
            gameService.joinGame(authToken, currentGameID, playerColor);
            ctx.status(200);
            ctx.result("{}");
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    //This will return a true if the item is null or blank spaces, which should cause an error. Else it will return false
    private boolean isInvalid(String item){
        return item == null || item.isBlank();
    }

    public void stop() {
        javalin.stop();
    }
}
