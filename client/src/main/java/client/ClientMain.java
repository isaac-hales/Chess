package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var facade = new ServerFacade(8080);
        new Repl(facade).run();
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}
