package chess;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    private final ArrayList<ChessPosition> moveList = new ArrayList<>();

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    private List<ChessPosition> PieceMoveCalculator(ChessPosition startPosition, ChessPiece type) {
        if (type.getPieceType() == ChessPiece.PieceType.BISHOP) {
            //return a list of positions if given a start position
            //+row,+col +row,-col -row,-col -row,+col
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() + i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() + i >= 8 || startPosition.getColumn() - i < 0 ) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() - i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() + i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getColumn() - i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() - i));
                }
            }
            return moveList;
        }
        else if (type.getPieceType() == ChessPiece.PieceType.KING) {
            List<ChessPosition> potentialKingMoves = List.of(new ChessPosition(1, 0),new ChessPosition(-1, 0),
                    new ChessPosition(0, 1), new ChessPosition(0, -1),new ChessPosition(1, 1),
                    new ChessPosition(1, -1), new ChessPosition(-1, 1),new ChessPosition(-1, -1));
            for ( ChessPosition moves : potentialKingMoves) {
                //Need to alter the code so that instead of i, it adds the first / second number of the pair in the list
                //Also need to check if the adjacent pieces that are capturable are adjacent or not.
                if (startPosition.getRow() + moves.getRow() < 0 || startPosition.getRow() + moves.getRow() >= 8 ||
                        startPosition.getColumn() + moves.getColumn() < 0 || startPosition.getColumn() + moves.getColumn() >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + moves.getRow(), startPosition.getColumn() + moves.getColumn()));
                }
            }
            return moveList;
        }
        else if (type.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            List<ChessPosition> potentialKnightMoves = List.of(new ChessPosition(2, 1), new ChessPosition(1, 2),
                    new ChessPosition(2, -1),new ChessPosition(1, -2),new ChessPosition(-2, 1),
                    new ChessPosition(-1, 2),new ChessPosition(-2, -1),new ChessPosition(-1, -2));
            for (ChessPosition move : potentialKnightMoves) {
                if (startPosition.getRow() + move.getRow() < 0 || startPosition.getRow() + move.getRow() >= 8 ||
                        startPosition.getColumn() + move.getColumn() < 0 || startPosition.getColumn() + move.getColumn() >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + move.getRow(), startPosition.getColumn() + move.getColumn()));
                }
            }
            return moveList;
        }
        else if (type.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (startPosition.getRow() == 6 && type.getTeamColor() == ChessGame.TeamColor.WHITE){
                moveList.add(new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()));
                moveList.add(new ChessPosition(startPosition.getRow()-2,startPosition.getColumn()));
            }
            else if (type.getTeamColor() == ChessGame.TeamColor.WHITE){
                moveList.add(new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()));
            }
            else if (startPosition.getRow() == 2 && type.getTeamColor() == ChessGame.TeamColor.BLACK){
                moveList.add(new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()));
                moveList.add(new ChessPosition(startPosition.getRow()+2,startPosition.getColumn()));
            }
            else if (type.getTeamColor() == ChessGame.TeamColor.BLACK){
                moveList.add(new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()));
            }
            return moveList;
        }
        else if (type.getPieceType() == ChessPiece.PieceType.QUEEN) {
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getRow() + i >= 8 ||
                        startPosition.getColumn() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() + i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getRow() + i >= 8 ||
                        startPosition.getColumn() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn() - i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getRow() + i >= 8 ||
                        startPosition.getColumn() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() + i));
                }
            }
            for (int i = 0; i < 8; i++) {
                if (startPosition.getRow() - i < 0 || startPosition.getRow() + i >= 8 ||
                        startPosition.getColumn() - i < 0 || startPosition.getColumn() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() - i,startPosition.getColumn() - i));
                }
            }
            for (int i = -7; i < 8; i++) {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn()));
                }
            }
            for (int i = -7; i < 8; i++) {
                if (startPosition.getColumn() + i < 0 || startPosition.getColumn() >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow(),startPosition.getColumn() + i));
                }
            }
            return moveList;
        }
        else if (type.getPieceType() == ChessPiece.PieceType.ROOK) {
            for (int i = -7; i < 8; i++) {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow() + i,startPosition.getColumn()));
                }
            }
            for (int i = -7; i < 8; i++) {
                if (startPosition.getColumn() + i < 0 || startPosition.getColumn() +i >= 8) {continue;}
                else {
                    moveList.add(new ChessPosition(startPosition.getRow(),startPosition.getColumn() + i));
                }
            }
        }
        else {
            throw new RuntimeException("Invalid Piece. What did you do?");
        }
        return moveList;
    }


    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        return String.format("%s%s", startPosition, endPosition);
    }
}

