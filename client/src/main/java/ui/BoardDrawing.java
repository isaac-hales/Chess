package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardDrawing {


    public static void drawBoard(ChessBoard board, boolean isWhitePerspective) {
        if (isWhitePerspective) {
            System.out.println(SET_BG_COLOR_DARK_GREY + "    a   b   c  d   e   f  g   h    " + RESET_BG_COLOR);
            for (int i = 8; i >= 1; i--) {
                System.out.print(SET_BG_COLOR_DARK_GREY + " " + i + " " + RESET_BG_COLOR);
                for (int j = 1; j < 9; j++) {
                    ChessPosition tempPosition = new ChessPosition(i,j);
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if ((i + j) % 2 != 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    if (tempPiece != null) {
                        if (tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            System.out.print(SET_TEXT_COLOR_BLUE);
                        }
                        else {
                            System.out.print(SET_TEXT_COLOR_MAGENTA);
                        }
                    }
                    System.out.print(getPieceSymbol(tempPiece));
                    System.out.print(RESET_TEXT_COLOR);
                    System.out.print(RESET_BG_COLOR);
                }
                System.out.print(SET_BG_COLOR_DARK_GREY + " " + i + " " + RESET_BG_COLOR);
                System.out.println();
            }
            System.out.println(SET_BG_COLOR_DARK_GREY + "    a   b   c  d   e   f  g   h    " + RESET_BG_COLOR);
        }
        else {
            for (int i = 1; i < 9; i++) {
                for (int j = 8; j >= 1; j--) {
                    ChessPosition tempPosition = new ChessPosition(i,j);
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if ((i + j) % 2 != 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                    else {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    if (tempPiece != null) {
                        if (tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            System.out.print(SET_TEXT_COLOR_BLUE);
                        }
                        else {
                            System.out.print(SET_TEXT_COLOR_MAGENTA);
                        }
                    }
                    System.out.print(getPieceSymbol(tempPiece));
                    System.out.print(RESET_TEXT_COLOR);
                    System.out.print(RESET_BG_COLOR);
                }
                System.out.println();
            }
        }
        // print top border with column letters
        // loop through rows
        //   print row number on left border
        //   loop through columns
        //     get piece at position
        //     set background color based on square color
        //     print piece symbol
        //   print row number on right border
        // print bottom border with column letters
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
