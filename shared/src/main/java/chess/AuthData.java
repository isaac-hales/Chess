package chess;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthData(
        @JsonProperty("username") String userName,
        @JsonProperty("authToken") String authToken
) {
    public static AuthData create(String userName) {
        return new AuthData(userName, java.util.UUID.randomUUID().toString());
    }
}