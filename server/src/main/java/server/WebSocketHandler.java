package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
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
    private final AuthDAOInterface authDAO;
    private final GameDAOInterface gameDAO;
    private final Map<Integer, Set<WsContext>> gameSessions = new HashMap<>();
    private final Gson gson = new Gson();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService, AuthDAOInterface authDAO, GameDAOInterface gameDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void onConnect(WsContext ctx) {

    }

    public void onMessage(WsMessageContext ctx) {
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

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) throws DataAccessException, InvalidMoveException {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        GameData currentGame = gameDAO.getGame(gameID);
        if (currentGame.game().isGameOver()) {
            ctx.send(gson.toJson(new ErrorMessage("Error: game is already over")));
            return;
        }
        MakeMoveCommand moveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);
        ChessMove move = moveCommand.getMove();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        String username = authData.username();
        ChessGame.TeamColor currentTurn = currentGame.game().getTeamTurn();
        currentGame.game().makeMove(move);
        gameDAO.updateGame(new GameData(gameID, currentGame.whiteUsername(),
                currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        //Sending LOAD_GAME to all clients
        broadcast(gameID, null, new LoadGameMessage(currentGame.game()));
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

    private void handleLeave(WsContext ctx, UserGameCommand command) throws DataAccessException {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        GameData currentGame = gameDAO.getGame(gameID);
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        String username = authData.username();
        if (username.equals(currentGame.whiteUsername())) {
            gameDAO.updateGame(new GameData(gameID, null,
                    currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        }
        else if (username.equals(currentGame.blackUsername())) {
            gameDAO.updateGame(new GameData(gameID, currentGame.whiteUsername(),
                    null, currentGame.gameName(), currentGame.game()));
        }
        gameSessions.get(gameID).remove(ctx);
        broadcast(gameID, null, new NotificationMessage("Player " + username + " has left the game."));
    }

    private void handleResign(WsContext ctx, UserGameCommand command) throws DataAccessException {
        String authToken = command.getAuthToken();
        Integer gameID = command.getGameID();
        GameData currentGame = gameDAO.getGame(gameID);
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            ctx.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        String username = authData.username();
        if (!username.equals(currentGame.whiteUsername()) && !username.equals(currentGame.blackUsername())) {
            ctx.send(gson.toJson(new ErrorMessage("Error: observers cannot resign. Though that would be funny")));
            return;
        }
        currentGame.game().setGameOver(true);
        gameDAO.updateGame(new GameData(gameID, currentGame.whiteUsername(),
                currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        broadcast(gameID, null, new NotificationMessage(username + " has resigned. That's the game!"));
    }

    public void onClose(WsContext ctx) {
        for (var entry: gameSessions.entrySet()) {
            entry.getValue().remove(ctx);
        }
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


}