package chess;

//All the data will be accessible by GameData.gameID, GameData.whiteUsername, GameData.blackUsername
/**
 * With this, Java can do the following:
 * A constructor that takes all five parameters
 * Accessor methods gameID, whiteUsername, blackUsername
 * gameName, ChessGame
 */
public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
}
