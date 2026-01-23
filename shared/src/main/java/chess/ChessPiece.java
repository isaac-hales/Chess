package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
        return PieceMoveCalculator(myPosition, board);
    }

    //Move it to its own class, for better code quality.
    public List<ChessMove> PieceMoveCalculator(ChessPosition startPosition, ChessBoard board) {
        final ArrayList<ChessMove> moveList = new ArrayList<>();
        if (this.type == ChessPiece.PieceType.BISHOP) {
            //Going Down, Right
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn() + i;
                if (newRow > 8 || newCol > 8){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}

            }
            //Going Up, Left
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn() - i;
                if (newRow > 8 || newCol < 1){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            //Going Down, right
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn() + i;
                if (newRow < 1 || newCol > 8){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);

                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn() - i;
                if (newRow < 1 || newCol < 1){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);

                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.KING) {
            List<ChessPosition> potentialKingMoves = List.of(new ChessPosition(1, 0),new ChessPosition(-1, 0),
                    new ChessPosition(0, 1), new ChessPosition(0, -1),new ChessPosition(1, 1),
                    new ChessPosition(1, -1), new ChessPosition(-1, 1),new ChessPosition(-1, -1));
            for ( ChessPosition moves : potentialKingMoves) {
                int newRow = startPosition.getRow() + moves.getRow();
                int newCol = startPosition.getColumn() + moves.getColumn();
                if (startPosition.getRow() + moves.getRow() < 1 || startPosition.getRow() + moves.getRow() > 8 ||
                        startPosition.getColumn() + moves.getColumn() < 1 || startPosition.getColumn() + moves.getColumn() > 8) {
                    continue;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);

                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
            }
            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.KNIGHT) {
            List<ChessPosition> potentialKnightMoves = List.of(new ChessPosition(2, 1), new ChessPosition(1, 2),
                    new ChessPosition(2, -1),new ChessPosition(1, -2),new ChessPosition(-2, 1),
                    new ChessPosition(-1, 2),new ChessPosition(-2, -1),new ChessPosition(-1, -2));
            for (ChessPosition moves : potentialKnightMoves) {
                int newRow = startPosition.getRow() + moves.getRow();
                int newCol = startPosition.getColumn() + moves.getColumn();
                if (startPosition.getRow() + moves.getRow() < 1 || startPosition.getRow() + moves.getRow() > 8 ||
                        startPosition.getColumn() + moves.getColumn() < 1 || startPosition.getColumn() + moves.getColumn() > 8) {
                    continue;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
            }
            return moveList;
        }
        //What should I put instead of null at the promotion piece spot?
        else if (this.type == ChessPiece.PieceType.PAWN) {
            if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                // WHITE pawns move UP (row increases: 2 -> 3 -> 4...)

                // Move forward one square
                if (startPosition.getRow() < 8) {
                    ChessPosition oneForward = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
                    ChessPiece pieceAhead = board.getPiece(oneForward);

                    if (pieceAhead == null) {  // Square must be empty
                        if (startPosition.getRow() + 1 == 8) {  // Promotion
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, oneForward, null));
                        }

                        // Double move from starting position
                        if (startPosition.getRow() == 2) {
                            ChessPosition twoForward = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
                            ChessPiece pieceTwoAhead = board.getPiece(twoForward);
                            if (pieceTwoAhead == null) {
                                moveList.add(new ChessMove(startPosition, twoForward, null));
                            }
                        }
                    }
                }

                // Diagonal capture left
                if (startPosition.getRow() < 8 && startPosition.getColumn() > 1) {
                    ChessPosition diagLeft = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
                    ChessPiece pieceLeft = board.getPiece(diagLeft);

                    if (pieceLeft != null && pieceLeft.getTeamColor() != this.pieceColor) {
                        if (startPosition.getRow() + 1 == 8) {  // Promotion
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, diagLeft, null));
                        }
                    }
                }

                // Diagonal capture right
                if (startPosition.getRow() < 8 && startPosition.getColumn() < 8) {
                    ChessPosition diagRight = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
                    ChessPiece pieceRight = board.getPiece(diagRight);

                    if (pieceRight != null && pieceRight.getTeamColor() != this.pieceColor) {
                        if (startPosition.getRow() + 1 == 8) {  // Promotion
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, diagRight, null));
                        }
                    }
                }
            }
            else if (this.getTeamColor() == ChessGame.TeamColor.BLACK) {
                // BLACK pawns move DOWN (row decreases: 7 -> 6 -> 5...)

                // Move forward one square
                if (startPosition.getRow() > 1) {
                    ChessPosition oneForward = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
                    ChessPiece pieceAhead = board.getPiece(oneForward);

                    if (pieceAhead == null) {  // Square must be empty
                        if (startPosition.getRow() - 1 == 1) {  // Promotion
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, oneForward, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, oneForward, null));
                        }

                        // Double move from starting position
                        if (startPosition.getRow() == 7) {
                            ChessPosition twoForward = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
                            ChessPiece pieceTwoAhead = board.getPiece(twoForward);
                            if (pieceTwoAhead == null) {
                                moveList.add(new ChessMove(startPosition, twoForward, null));
                            }
                        }
                    }
                }

                // Diagonal capture left
                if (startPosition.getRow() > 1 && startPosition.getColumn() > 1) {
                    ChessPosition diagLeft = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
                    ChessPiece pieceLeft = board.getPiece(diagLeft);

                    if (pieceLeft != null && pieceLeft.getTeamColor() != this.pieceColor) {
                        if (startPosition.getRow() - 1 == 1) {  // Promotion
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, diagLeft, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, diagLeft, null));
                        }
                    }
                }

                // Diagonal capture right
                if (startPosition.getRow() > 1 && startPosition.getColumn() < 8) {
                    ChessPosition diagRight = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
                    ChessPiece pieceRight = board.getPiece(diagRight);

                    if (pieceRight != null && pieceRight.getTeamColor() != this.pieceColor) {
                        if (startPosition.getRow() - 1 == 1) {  // Promotion
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, diagRight, PieceType.KNIGHT));
                        } else {
                            moveList.add(new ChessMove(startPosition, diagRight, null));
                        }
                    }
                }
            }
        }

        else if (this.type == ChessPiece.PieceType.QUEEN) {
            //Bishop Moves
            //Going Down, Right
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn() + i;
                if (newRow > 8 || newCol > 8){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}

            }
            //Going Up, Left
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn() - i;
                if (newRow > 8 || newCol < 1){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            //Going Down, right
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn() + i;
                if (newRow < 1 || newCol > 8){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);

                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            //Going Down, left
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn() - i;
                if (newRow < 1 || newCol < 1){break;}
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);

                //Free spot, good to go.
                if (adjacentPiece == null){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor){
                    moveList.add(new ChessMove(startPosition, newPosition,null));
                    break;
                }
                //This is a friendly piece.
                else {break;}
            }
            //Rook Moves
            //Going up the board, row subtraction
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn();
                if (newRow < 1) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going down the board row addition
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn();
                if (newRow > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going Right column addition
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow();
                int newCol = startPosition.getColumn() + i;
                if (newCol > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going Left subtracting Column
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow();
                int newCol = startPosition.getColumn() - i;
                if (newCol < 1) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }

            return moveList;
        }
        else if (this.type == ChessPiece.PieceType.ROOK) {
            //Going up the board row subtraction
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() - i;
                int newCol = startPosition.getColumn();
                if (newRow < 1) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going down the board row addition
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow() + i;
                int newCol = startPosition.getColumn();
                if (newRow > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going Right column addition
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow();
                int newCol = startPosition.getColumn() + i;
                if (newCol > 8) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            //Going Left subtracting Column
            for (int i = 1; i < 8; i++) {
                int newRow = startPosition.getRow();
                int newCol = startPosition.getColumn() - i;
                if (newCol < 1) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece adjacentPiece = board.getPiece(newPosition);
                //Free spot, good to go.
                if (adjacentPiece == null) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
                //Capturing an enemy piece
                else if (adjacentPiece.getTeamColor() != this.pieceColor) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                    break;
                }
                //This is a friendly piece.
                else {
                    break;
                }
            }
            return moveList;
        }
        else {
            throw new RuntimeException("INVALID PIECE! WHAT DID YOU DO?!?!");
        }
        return moveList;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
