package client;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.ContainerProvider;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.net.URI;

@ClientEndpoint //Lets the server know that this is a WebSocket client
public class WebSocketCommunicator {
    private Session session;
    private final client.MessageHandler messageHandler;


    public WebSocketCommunicator(MessageHandler messageHandler, String serverURL) throws Exception {
        this.messageHandler = messageHandler;
        var webSocketContainer = ContainerProvider.getWebSocketContainer();
        webSocketContainer.connectToServer(this, new URI(serverURL + "/ws"));
    }

    public void sendMessage(String message) throws Exception {
        session.getBasicRemote().sendText(message);
    }

    public void connect(String authToken, int gameID) throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        sendMessage(new Gson().toJson(command));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception{
        var command = new MakeMoveCommand(authToken, gameID, move);
        sendMessage(new Gson().toJson(command));
    }

    public void leave(String authToken, int gameID) throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        sendMessage(new Gson().toJson(command));
    }

    public void resign(String authToken, int gameID) throws Exception {
        var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        sendMessage(new Gson().toJson(command));
    }

    @OnOpen //Starts the session
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage //For messages and stuff
    public void onMessage(String message) {
        messageHandler.onMessage(message);
    }


}
