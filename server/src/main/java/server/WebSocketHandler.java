package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import io.javalin.websocket.WsContext;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketHandler {
    private final GameService gameService;
    private final AuthDAOInterface authDAO;
    private final GameDAOInterface gameDAO;
    private final Map<Integer, Set<WsContext>> gameSessions = new HashMap<>();
    private final Gson gson = new Gson();

    public WebSocketHandler(GameService gameService, AuthDAOInterface authDAO, GameDAOInterface gameDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void onConnect(WsContext ctx) {

    }

    public void onMessage(WsContext ctx) {
        try {
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(ctx, command);
                case MAKE_MOVE -> handleMakeMove(ctx, command);
                case LEAVE -> handleLeave(ctx, command);
                case RESIGN -> handleResign(ctx, command);
            }
        } catch (DataAccessException e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: " + e.getMessage())));
        } catch (InvalidMoveException e) {
            ctx.send(gson.toJson(new ErrorMessage("Error: invalid move - " + e.getMessage())));
        }
    }

    private void handleConnect(WsContext ctx, UserGameCommand command) throws DataAccessException {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        String username = authData.username();
        if (username == null) { //invalid authToken
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        gameSessions.computeIfAbsent(gameID, k -> new HashSet<>()).add(ctx);
        GameData gameData = gameDAO.getGame(gameID);
        sendToOne(ctx, new LoadGameMessage(gameData.game()));
        broadcast(gameID, ctx, new NotificationMessage(username + " has joined the game"));
    }

    private void handleMakeMove(WsContext ctx, UserGameCommand command) throws DataAccessException, InvalidMoveException {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        GameData currentGame = gameDAO.getGame(gameID);
        MakeMoveCommand moveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);
        ChessMove move = moveCommand.getMove();
        AuthData authData = authDAO.getAuth(authToken);
        if (!validateAuthtoken(ctx,authToken)) { //Invalid authToken
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        
        ChessGame.TeamColor currentTurn = currentGame.game().getTeamTurn();
        currentGame.game().makeMove(move);
        gameDAO.updateGame(new GameData(gameID, currentGame.whiteUsername(),
                currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        //Sending LOAD_GAME to all clients
        broadcast(gameID, null, new LoadGameMessage(currentGame.game()));
        String username = authData.username();
        broadcast(gameID, ctx, new NotificationMessage( username + " has made the move " + move));
        ChessGame.TeamColor opponent = currentGame.game().getTeamTurn(); // the other player's color
        if (currentGame.game().isInCheckmate(opponent)) {
            broadcast(gameID, null, new NotificationMessage(opponent + " is in checkmate!"));
        } else if (currentGame.game().isInCheck(opponent)) {
            broadcast(gameID, null, new NotificationMessage(opponent + " is in check!"));
        } else if (currentGame.game().isInStalemate(opponent)) {
            broadcast(gameID, null, new NotificationMessage("Stalemate!"));
        }
    }

    private void handleLeave(WsContext ctx, UserGameCommand command) {

    }

    private void handleResign(WsContext ctx, UserGameCommand command) {

    }

    public void onClose(WsContext ctx) {

    }

    public void sendToOne(WsContext ctx, ServerMessage message) {
        ctx.send(gson.toJson(message));
    }

    public void broadcast(Integer gameID, WsContext exclude, ServerMessage message){
        var sessions = gameSessions.get(gameID);
        if (sessions != null) {
            for (var session: sessions) {
                if (!session.equals(exclude)) {
                    session.send(gson.toJson(message));
                }
            }
        }
    }

    public boolean validateAuthtoken(WsContext ctx, String authData) {
        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return false;
        }
        return true;
    }


}
