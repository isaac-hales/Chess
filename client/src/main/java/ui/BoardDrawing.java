package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardDrawing {


    public static void drawBoard(ChessBoard board, boolean isWhitePerspective) {
        String borderLetters = isWhitePerspective ?
                "    a  b  c  d  e  f  g  h    " :
                "    h  g  f  e  d  c  b  a    ";
        System.out.println(SET_BG_COLOR_DARK_GREY + borderLetters + RESET_BG_COLOR);

        int rowStart = isWhitePerspective ? 8 : 1;
        int rowEnd = isWhitePerspective ? 0 : 9;
        int rowStep = isWhitePerspective ? -1 : 1;

        for (int i = rowStart; i != rowEnd; i += rowStep) {
            printRow(board, i, isWhitePerspective);
        }
        System.out.println(SET_BG_COLOR_DARK_GREY + borderLetters + RESET_BG_COLOR);
    }

    private static void printRow(ChessBoard board, int row, boolean isWhitePerspective) {
        System.out.print(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
        int colStart = isWhitePerspective ? 1 : 8;
        int colEnd = isWhitePerspective ? 9 : 0;
        int colStep = isWhitePerspective ? 1 : -1;
        for (int j = colStart; j != colEnd; j += colStep) {
            printSquare(board.getPiece(new ChessPosition(row, j)), row, j);
        }
        System.out.print(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
        System.out.println();
    }

    private static void printSquare(ChessPiece piece, int row, int col) {
        System.out.print((row + col) % 2 != 0 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_DARK_GREEN);
        if (piece != null) {
            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                    SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_MAGENTA);
        }
        System.out.print(getPieceSymbol(piece));
        System.out.print(RESET_TEXT_COLOR);
        System.out.print(RESET_BG_COLOR);
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
