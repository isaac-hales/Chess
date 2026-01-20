package chess;

import java.util.List;

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
    private final List moveList;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    private PieceMoveCalculator(ChessPosition startPosition, ChessPiece type) {
        if (type == ChessPiece.PieceType.BISHOP) {
            //return a list of positions if given a start position
            //+row,+col +row,-col -row,-col -row,+col
            for (int i = 0; i < 8; i++) {
                //Should this be its own function to call whenever, or leave it be?
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8 || startPosition.getColumn() + i < 0 || startPosition.getColumn() >= 8) {
                    //pass? Or whatever the equivalent is in Java
                }
                else {
                    moveList.push(startPosition.getRow() + i, startPosition.getColumn() + i);
                }
            }
            return moveList;
        }
        else if (type == ChessPiece.PieceType.KING) {
            //8 ways a king moves
            //+row, -row, +col, -col, +row,+col, +row,-col, -row,+col, -row,-col
            List.of([(1, 0),(-1, 0),(0, 1),(0, -1),(1, 1),(1, -1),(-1, 1),(-1, -1)]);
            for (move in potentialKingMoves) {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8 || startPosition.getColumn() + i < 0 || startPosition.getColumn() >= 8) {
                    //pass? Or whatever the equivalent is in Java
                }
                else {
                    moveList.push(startPosition.getRow() + i, startPosition.getColumn() + i);
                }
            }
        }
        else if (type == ChessPiece.PieceType.KNIGHT) {
            //8 ways a king moves
            //
            List.of(potentialKnightMoves = [(2, 1),(1, 2),(2, -1),(1, -2),(-2, 1),(-1, 2),(-2, -1),(-1, -2)]);
            for move in potentialKnightMoves {
                if (startPosition.getRow() + i < 0 || startPosition.getRow() + i >= 8 || startPosition.getColumn() + i < 0 || startPosition.getColumn() >= 8) {
                    //pass? Or whatever the equivalent is in Java
                }
                else {
                    moveList.push(startPosition.getRow() + i, startPosition.getColumn() + i);
                }
            }
        }
        else if (type == ChessPiece.PieceType.PAWN) {
            return null;
        }
        else if (type == ChessPiece.PieceType.QUEEN) {
            return null;
        }
        else if (type == ChessPiece.PieceType.ROOK) {
            return null;
            for (int i = 0; i < 8; i++) {

            }
        }
        else {
            throw new RuntimeException("Invalid Piece. What did you do?");
        }
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

