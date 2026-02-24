package chess;
import java.util.UUID;

public record AuthData(String userName, String authToken) {

    //Generates the authToken,
    public static AuthData create(String userName) {
        return new AuthData(userName, UUID.randomUUID().toString());
    }
    //Class starts this Thursday, at 7 Talmage 1110
}
