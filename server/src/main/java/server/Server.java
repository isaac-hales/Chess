package server;

import chess.AuthData;
import chess.GameData;
import chess.UserData;
import dataaccess.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.ServiceException;
import java.util.Collection;
import java.util.Map;
import com.google.gson.Gson;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;

public class Server {

    private final Javalin javalin;
    //Storage for the users logging in, and the authTokens
    private final UserDAOInterface userDAO = new SQLUserDAO();
    private final GameDAOInterface gameDAO = new SQLGameDAO();
    private final AuthDAOInterface authDAO = new SQLAuthDAO();

    //Giving the DAOs to the Services, like army, navy
    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(authDAO, gameDAO);
    private final ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);

    private final WebSocketHandler webSocketHandler = new WebSocketHandler(gameService, authDAO, gameDAO);

    public Server() {
        Gson gson = new Gson();
        JsonMapper gsonMapper = createGsonMapper(gson);
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(gsonMapper);
        });
        javalin.exception(ServiceException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());
            ctx.json(Map.of("message", e.getMessage()));
        });
        registerEndpoints();
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private JsonMapper createGsonMapper(Gson gson) {
        return new JsonMapper() {
            @NotNull @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }
            @NotNull @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };
    }

    private void registerEndpoints() {
        javalin.delete("/db", (ctx) -> handleClear(ctx));
        javalin.post("/user", (ctx) -> handleRegister(ctx));
        javalin.post("/session", (ctx) -> handleLogin(ctx));
        javalin.delete("/session", (ctx) -> handleLogout(ctx));
        javalin.get("/game", (ctx) -> handleListGames(ctx));
        javalin.post("/game", (ctx) -> handleCreateGame(ctx));
        javalin.put("/game", (ctx) -> handleJoinGame(ctx));
        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler::onConnect);
            ws.onMessage(webSocketHandler::onMessage);
            ws.onClose(webSocketHandler::onClose);
        });
    }

    private void handleClear(io.javalin.http.Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    private void handleRegister(io.javalin.http.Context ctx) {
        UserData user = ctx.bodyAsClass(UserData.class);
        if (isInvalid(user.username()) || isInvalid(user.password()) || isInvalid(user.email())) {
            ctx.status(400);
            ctx.json(Map.of("message", "Error: bad request"));
            return;
        }
        AuthData result = userService.register(user);
        ctx.status(200);
        ctx.json(result);
    }

    private void handleLogin(io.javalin.http.Context ctx) {
        UserData user = ctx.bodyAsClass(UserData.class);
        if (isInvalid(user.username()) || isInvalid(user.password())) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
            return;
        }
        AuthData result = userService.login(user);
        ctx.status(200);
        ctx.json(result);
    }

    private void handleLogout(io.javalin.http.Context ctx) {
        String authToken = ctx.header("authorization");
        userService.logout(authToken);
        ctx.status(200);
        ctx.result("{}");
    }

    private void handleListGames(io.javalin.http.Context ctx) {
        String authToken = ctx.header("authorization");
        Collection<GameData> result = gameService.listGames(authToken);
        ctx.status(200);
        ctx.json(Map.of("games", result));
    }

    private void handleCreateGame(io.javalin.http.Context ctx) {
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
    }

    private void handleJoinGame(io.javalin.http.Context ctx) {
        String authToken = ctx.header("authorization");
        var body = ctx.bodyAsClass(Map.class);
        Object gameIDObj = body.get("gameID");
        String playerColor = (String) body.get("playerColor");
        if (gameIDObj == null) {
            ctx.status(400);
            ctx.json(Map.of("message", "Error: bad request"));
            return;
        }
        int currentGameID = ((Number) gameIDObj).intValue();
        gameService.joinGame(authToken, currentGameID, playerColor);
        ctx.status(200);
        ctx.result("{}");
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
