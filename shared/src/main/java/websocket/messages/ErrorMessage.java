package websocket.messages;

public class ErrorMessage extends ServerMessage {
    ServerMessageType serverMessageType;
    String errorMessage;

    ErrorMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getString(){
        return errorMessage;
    }

}
