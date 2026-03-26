package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.GameData;
import client.ServerFacade;

import java.util.Collection;
import java.util.List;

import static ui.BoardDrawing.drawBoard;

public class PostloginUI {
    private final ServerFacade facade;
    private String authToken = null;

    public PostloginUI(ServerFacade facade, String authToken) {
        this.facade = facade;
        this.authToken = authToken;
    }

    public String eval(String input) {
        var parts = input.split(" ");
        var cmd = parts[0].toLowerCase().trim();
        switch (cmd) {
            case "help" -> { return
                    """
                            create <NAME> - a game
                            list - games
                            join <ID> [WHITE|BLACK] - a game
                            observe <ID> - a game
                            logout - when you are done
                            quit - playing chess
                            help - with possible commands
                            """; }
            case "create" -> {
                if (parts.length != 2) {
                    return "Usage: create <NAME>";
                }
                try {
                    int gameID = facade.createGame(parts[1], authToken);
                    return "Game created with ID: " + gameID;
                }
                catch (Exception e) {
                    return "Create failed: " + e.getMessage();
                }
            }
            case "list" -> {
                try {
                    Collection<GameData> tempList = facade.listGames(authToken);
                    StringBuilder allGames = new StringBuilder();
                    for (GameData game : tempList) {
                        allGames.append(game.gameName())
                                .append(" | White: ").append(game.whiteUsername() != null ? game.whiteUsername() : "empty")
                                .append(" | Black: ").append(game.blackUsername() != null ? game.blackUsername() : "empty")
                                .append("\n");
                    }
                    return allGames.toString();
                }
                catch (Exception e) {
                    return "Login failed: " + e.getMessage();
                }
            }
            case "join" -> {
                if (parts.length != 3) {
                    return "Usage: join <ID> [WHITE|BLACK]";
                }
                try {
                    facade.joinGame(authToken, Integer.parseInt(parts[1]), parts[2]);
                    boolean isWhite = parts[2].equalsIgnoreCase("WHITE");
                    BoardDrawing.drawBoard(new ChessGame().getBoard(), isWhite);
                    return "Joined game " + parts[1];
                } catch (Exception e) {
                    return "Join failed: " + e.getMessage();
                }
            }
            case "observe" -> {
                if (parts.length != 2) {
                    return "Usage: observe <" + parts[1] + ">";
                }
                BoardDrawing.drawBoard(new ChessGame().getBoard(), true);
                return "Observing game " + parts[1];
            }
            case "logout" -> {
                try {
                    facade.logout(authToken);
                    authToken = null;
                    System.out.println("Logged out successfully!");
                    return "logout";
                }
                catch (Exception e) {
                    return "Logout failed: " + e.getMessage();
                }

            }
            case "quit" -> {
                if (parts.length == 1) {
                    return "quit";
                }
            }
            default -> { return "Unknown command. Type 'help' for options."; }
        }
        return "Unknown error. Type 'help' for options.";
    }

}
