package server;

import dataaccess.AuthDAOInterface;
import io.javalin.websocket.WsContext;
import service.GameService;

public class WebSocketHandler {
    GameService gameService;
    AuthDAOInterface authDAO;

    public WebSocketHandler(GameService gameService, AuthDAOInterface authDAO) {
        this.gameService = gameService;
        this.authDAO = authDAO;
    }

    public void onConnect(WsContext ctx) {

    }

    public void onMessage(WsContext ctx) {

    }

    public void onClose(WsContext ctx) {

    }

}
