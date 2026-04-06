package ui;

import chess.*;
import client.MessageHandler;
import client.WebSocketCommunicator;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.Scanner;

public class GameplayUI implements MessageHandler {
    private final WebSocketCommunicator ws;
    private final String authToken;
    private final String username;
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    private ChessGame currentGame;
    private final Gson gson = new Gson();

    public GameplayUI(String serverUrl, String authToken, String username, int gameID, ChessGame.TeamColor playerColor) throws Exception {
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.ws = new WebSocketCommunicator(this, serverUrl);
    }

    public String eval(String input) throws Exception {
        var parts = input.split(" ");
        var cmd = parts[0].toLowerCase().trim();
        switch (cmd) {
            case "help" -> { return
                        """
                            help - show available commands
                            redraw - redraw the board
                            leave - leave the game
                            move - make a move
                            resign - resign the game
                            highlight - highlight legal moves for a piece
                            """; }
            case "redraw" -> {
                                BoardDrawing.drawBoard(currentGame.getBoard(), playerColor != ChessGame.TeamColor.BLACK, null, null);
                                return "";
                                }
            case "leave" -> {
                try {
                    ws.leave(authToken, gameID);
                    return "leave";
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }
            case "move" -> {
                if (parts.length < 3) {
                    return "Usage: move <FROM> <TO> (move e2 e4)";
                }
                try {
                    ChessPosition from = parsePosition(parts[1]);
                    ChessPosition to = parsePosition(parts[2]);
                    ChessPiece.PieceType promotion = parsePromotion(parts);
                    ChessMove move = new ChessMove(from, to, promotion);
                    ws.makeMove(authToken, gameID, move);
                    return "";
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }
            case "resign" -> {
                System.out.print("Are you sure you want to resign? (input yes / no):");
                Scanner scanner = new Scanner(System.in);
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    ws.resign(authToken,gameID);
                    return "You have resigned. Better luck next time!";
                }
                return "Resign cancelled.";
            }
            case "highlight" -> {
                if (parts.length != 2) {
                    return "Usage: highlight <POSITION> (e.g. highlight e2)";
                }
                ChessPosition currentPosition = parsePosition(parts[1]);
                Collection<ChessMove> legalMoves = currentGame.validMoves(currentPosition);
                boolean isWhite = playerColor != ChessGame.TeamColor.BLACK;
                BoardDrawing.drawBoard(currentGame.getBoard(), isWhite, currentPosition, legalMoves);
                return "";
            }
            default -> {
                return "Unknown command. Type 'help' for options.";
            }
        }

    }

    public void connect() throws Exception {
        ws.connect(authToken, gameID);
    }

    @Override
    public void onMessage(String message) {
        var serverMessage = gson.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                var loadGame = gson.fromJson(message, LoadGameMessage.class);
                currentGame = loadGame.getGame();
                boolean isWhite = playerColor != ChessGame.TeamColor.BLACK;
                BoardDrawing.drawBoard(currentGame.getBoard(), isWhite, null, null);
            }
            case NOTIFICATION -> {
                var notification = gson.fromJson(message, NotificationMessage.class);
                System.out.println("NOTIFICATION: " + notification.getMessage());
            }
            case ERROR -> {
                var error = gson.fromJson(message, ErrorMessage.class);
                System.out.println("ERROR: " + error.getErrorMessage());
            }
        }
    }

    //Helper function to turn the input into chess positions / moves
    private ChessPosition parsePosition(String pos) {
        int col = pos.charAt(0) - 'a' + 1; //'a' = 1, 'b' = 2, yada yada
        int row = Integer.parseInt(String.valueOf(pos.charAt(1)));
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType parsePromotion(String[] parts) {
        if (parts.length != 4) {
            return null;
        }
        return switch (parts[3].toLowerCase()) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
    }
}
