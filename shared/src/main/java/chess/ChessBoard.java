package chess;

import base.ChessPieces;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //Java Arrays are 0 based, but the tests are 1 based.
    ChessPiece[][] squares = new ChessPiece[8][8]; //This is allocating the memory for it
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
                        //Position is in tuple, piece name is all caps i.e. QUEEN ROOK BISHOP KNIGHT KING PAWN
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (squares[position.getRow()-1][position.getColumn()-1] == ChessPiece.PieceType) {
            return squares[position.getRow()-1][position.getColumn()-1];
        }
        else {
            return null;
        }
    }

     /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
        //Black Pieces
        addPiece([1,1],ChessPiece.PieceType.ROOK);
        addPiece([1,2],ChessPiece.PieceType.KNIGHT);
        addPiece([1,3],ChessPiece.PieceType.BISHOP);
        addPiece([1,4],ChessPiece.PieceType.QUEEN);
        addPiece([1,5],ChessPiece.PieceType.KING);
        addPiece([1,6],ChessPiece.PieceType.BISHOP);
        addPiece([1,7],ChessPiece.PieceType.KNIGHT);
        addPiece([1,8],ChessPiece.PieceType.ROOK);
        //Black Pawns
        addPiece([2,1],ChessPiece.PieceType.PAWN);
        addPiece([2,2],ChessPiece.PieceType.PAWN);
        addPiece([2,3],ChessPiece.PieceType.PAWN);
        addPiece([2,4],ChessPiece.PieceType.PAWN);
        addPiece([2,5],ChessPiece.PieceType.PAWN);
        addPiece([2,6],ChessPiece.PieceType.PAWN);
        addPiece([2,7],ChessPiece.PieceType.PAWN);
        addPiece([2,8],ChessPiece.PieceType.PAWN);

        //White Pieces
        addPiece([8,1],ChessPiece.PieceType.ROOK);
        addPiece([8,2],ChessPiece.PieceType.KNIGHT);
        addPiece([8,3],ChessPiece.PieceType.BISHOP);
        addPiece([8,4],ChessPiece.PieceType.QUEEN);
        addPiece([8,5],ChessPiece.PieceType.KING);
        addPiece([8,6],ChessPiece.PieceType.BISHOP);
        addPiece([8,7],ChessPiece.PieceType.KNIGHT);
        addPiece([8,8],ChessPiece.PieceType.ROOK);
        //White Pawns
        addPiece([7,1],ChessPiece.PieceType.PAWN);
        addPiece([7,2],ChessPiece.PieceType.PAWN);
        addPiece([7,3],ChessPiece.PieceType.PAWN);
        addPiece([7,4],ChessPiece.PieceType.PAWN);
        addPiece([7,5],ChessPiece.PieceType.PAWN);
        addPiece([7,6],ChessPiece.PieceType.PAWN);
        addPiece([7,7],ChessPiece.PieceType.PAWN);
        addPiece([7,8],ChessPiece.PieceType.PAWN);
    }
}
