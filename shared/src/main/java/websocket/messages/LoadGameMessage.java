package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    ServerMessageType serverMessageType;
    String NotificationMessage;
    String ErrorMessage;
    ChessGame game;
    
    LoadGameMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }


    public ChessGame getGame() {
        return game;
    }


}
