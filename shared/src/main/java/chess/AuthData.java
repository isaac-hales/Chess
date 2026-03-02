package chess;

public record AuthData(
        String authToken,
        String username
)
{
    public static AuthData create(String userName) {
        return new AuthData(java.util.UUID.randomUUID().toString(), userName);
    }
}