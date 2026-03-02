package chess;

public record AuthData(String userName, String authToken
) {
    public static AuthData create(String userName) {
        return new AuthData(userName, java.util.UUID.randomUUID().toString());
    }
}