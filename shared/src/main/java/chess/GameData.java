package chess;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GameData(
        @JsonProperty("gameID") int gameID,
        @JsonProperty("whiteUsername") String whiteUsername,
        @JsonProperty("blackUsername") String blackUsername,
        @JsonProperty("gameName") String gameName,
        @JsonProperty("game") ChessGame game
) {}