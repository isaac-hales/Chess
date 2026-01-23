package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    final ChessGame.TeamColor pieceColor;
    final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    //Update to get all the different pieces
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return PieceMoveCalculator(myPosition);
    }

    //Move it to its own class, for better code quality.
    public List<ChessMove> PieceMoveCalculator(ChessPosition startPosition) {
        final ArrayList<ChessMove> moveList = new ArrayList<>();
        if (this.type == ChessPiece.PieceType.BISHOP) {
            //return a list of positions if given a start position
            //+row,+col +row,-col -row,-col -row,+col
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i), null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() - i < 0 ) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() - i),null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() + i),null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() - i < 0) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() - i),null));
                }
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.KING) {
            List<ChessPosition> potentialKingMoves = List.of(new ChessPosition(1, 0),new ChessPosition(-1, 0),
                    new ChessPosition(0, 1), new ChessPosition(0, -1),new ChessPosition(1, 1),
                    new ChessPosition(1, -1), new ChessPosition(-1, 1),new ChessPosition(-1, -1));
            for ( ChessPosition moves : potentialKingMoves) {
                //Need to alter the code so that instead of i, it adds the first / second number of the pair in the list
                //Also need to check if the adjacent pieces that are capturable are adjacent or not.
                if (startPosition.getRow() + moves.getRow() < 0 || startPosition.getRow() + moves.getRow() >= 8 ||
                        startPosition.getColumn() + moves.getColumn() < 0 || startPosition.getColumn() + moves.getColumn() >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() + moves.getRow(), startPosition.getColumn() + moves.getColumn()),null));
                }
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.KNIGHT) {
            List<ChessPosition> potentialKnightMoves = List.of(new ChessPosition(2, 1), new ChessPosition(1, 2),
                    new ChessPosition(2, -1),new ChessPosition(1, -2),new ChessPosition(-2, 1),
                    new ChessPosition(-1, 2),new ChessPosition(-2, -1),new ChessPosition(-1, -2));
            for (ChessPosition move : potentialKnightMoves) {
                if (startPosition.getRow() + move.getRow() < 0 || startPosition.getRow() + move.getRow() >= 8 ||
                        startPosition.getColumn() + move.getColumn() < 0 || startPosition.getColumn() + move.getColumn() >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() + move.getRow(), startPosition.getColumn() + move.getColumn()),null));
                }
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.PAWN) {
            if (startPosition.getRow() == 7 && this.getTeamColor() == ChessGame.TeamColor.WHITE){
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()),null));
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()-2,startPosition.getColumn()),null));
            }
            else if (this.getTeamColor() == ChessGame.TeamColor.WHITE && startPosition.getRow() != 1){
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()),null));
            }
            else if (startPosition.getRow() == 2 && this.getTeamColor() == ChessGame.TeamColor.BLACK){
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()),null));
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()+2,startPosition.getColumn()),null));
            }
            else if (this.getTeamColor() == ChessGame.TeamColor.BLACK && startPosition.getRow() != 8){
                moveList.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()),null));
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.QUEEN) {
            //Bishop Moves
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() + i), null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() - i < 0) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() - i), null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() + i), null));
                }
            }
            for (int i = 1; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() - i < 0 ) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() - i), null));
                }
            }
            //Rook Moves
            for (int i = -7; i < 8; i++) {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i,startPosition.getColumn()), null));
                }
            }
            for (int i = -7; i < 8; i++) {
                if (startPosition.getColumn() + i < 0 || startPosition.getColumn() +i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),startPosition.getColumn() + i), null));
                }
            }

            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.ROOK) {
            for (int i = -7; i < 8; i++) {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + i,startPosition.getColumn()), null));
                }
            }
            for (int i = -7; i < 8; i++) {
                if (startPosition.getColumn() + i < 0 || startPosition.getColumn() +i >= 8) {continue;}
                else {
                    moveList.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),startPosition.getColumn() + i), null));
                }
            }
        }
        else {
            throw new RuntimeException("INVALID PIECE! WHAT DID YOU DO?!?!");
        }
        return moveList;
    }
}
