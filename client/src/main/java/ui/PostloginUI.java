package ui;

import chess.ChessGame;
import chess.GameData;
import client.ServerFacade;
import client.WebSocketCommunicator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostloginUI {
    private final ServerFacade facade;
    private String authToken = null;
    private List<GameData> lastGameList = new ArrayList<>();
    private GameplayUI gameplayUI = null;

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
                    int number = 1;
                    for (GameData game : tempList) {
                        allGames.append(number).append(". ").append(game.gameName())
                            .append(" | White: ").append(game.whiteUsername() != null ? game.whiteUsername() : "empty")
                            .append(" | Black: ").append(game.blackUsername() != null ? game.blackUsername() : "empty")
                            .append("\n");
                            number++;
                        }
                        lastGameList = new ArrayList<>(tempList);
                    return allGames.toString();
                }
                catch (Exception e) {
                    return "Login failed: " + e.getMessage();
                }
            }
            case "join" -> {
                if (parts.length != 3) {
                    return "Usage: join <ID> [WHITE / BLACK]";
                }
                try {
                    int listNumber = Integer.parseInt(parts[1]);
                    int realGameID = lastGameList.get(listNumber - 1).gameID();
                    facade.joinGame(authToken, realGameID, parts[2]);
                    ChessGame.TeamColor color = parts[2].equalsIgnoreCase("WHITE") ?
                            ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    gameplayUI = new GameplayUI(facade.getServerUrl(), authToken, "", realGameID, color);
                    gameplayUI.connect();
                    return "Joined game " + parts[1];
                } catch (Exception e) {
                    return "Join failed: " + e.getMessage();
                }
            }
            case "observe" -> {
                if (parts.length != 2) {
                    return "Usage: observe <ID>";
                } try {
                    int listNumber = Integer.parseInt(parts[1]);
                    int realGameID = lastGameList.get(listNumber - 1).gameID();
                    gameplayUI = new GameplayUI(facade.getServerUrl(), authToken, "", realGameID, ChessGame.TeamColor.WHITE);
                    gameplayUI.connect();
                    return "Observing game " + parts[1];
                } catch (NumberFormatException e) {
                    return "Please enter a valid number for gameID, and not the game name. ";
                } catch (IndexOutOfBoundsException e) {
                    return "Please choose a number between 1 and " + lastGameList.size() + ". " +
                            "The number you gave, " + parts[1] + ", is out of bounds.";
                } catch (Exception e) {
                    return "Observe failed: " + e.getMessage() + " - Please use a number!";
                }
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

    public GameplayUI getGameplayUI() {
        return gameplayUI;
    }

    public void clearGameplayUI() {
        gameplayUI = null;
    }

}
