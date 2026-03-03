package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


public class ChessGame {
    ChessBoard gameBoard = new ChessBoard();
    TeamColor teamTurn; 
    //int turnCounter;

    //This is a constructor, so any function that we need to run at the start, we run here.
    public ChessGame() {
        gameBoard.resetBoard(); //Not a mistake. It should set up the default position, by default.
        teamTurn = TeamColor.WHITE; //First turn belongs to white team.
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }


    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece tempPiece = gameBoard.getPiece(startPosition);
        if (tempPiece == null){
            return null;
        }
        //We're getting all the moves that the piece can make, regardless of check / checkmate.
        Collection<ChessMove> potentialMoves = tempPiece.pieceMoves(gameBoard, startPosition);
        ArrayList<ChessMove> legalMoves = new ArrayList<>();

        //Now we're going through each move, and checking if it leaves out king in check / checkmate.
        for (ChessMove move : potentialMoves) {
            ChessPiece capturedPiece = gameBoard.getPiece(move.getEndPosition());

            //Making the move on the board
            gameBoard.addPiece(move.getEndPosition(), tempPiece);
            gameBoard.addPiece(move.getStartPosition(), null);

            //checking if the king is in check.
            boolean kingInCheck = isInCheck(tempPiece.getTeamColor());

            //Undoing the move.
            gameBoard.addPiece(move.getStartPosition(), tempPiece);
            //Putting the capturedPiece back in its original position.
            gameBoard.addPiece(move.getEndPosition(), capturedPiece);
            //Adding it if the king is not in check.
            if (!kingInCheck) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece tempPiece = gameBoard.getPiece(move.getStartPosition());
        if (tempPiece == null){
            throw new InvalidMoveException("No piece was selected. ");
        }
        final ArrayList<ChessMove> potentialMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (tempPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not currently " + tempPiece.getTeamColor()+"s turn yet.");
        }
        for (ChessMove legalMove: potentialMoves){
            if (legalMove.equals(move)){
                gameBoard.addPiece(move.getStartPosition(),null);
                //Checking if it's a pawn, and then doing promotion if so.
                if (move.getPromotionPiece() != null){
                    ChessPiece promotedPiece = new ChessPiece(tempPiece.getTeamColor(), move.getPromotionPiece());
                    gameBoard.addPiece(move.getEndPosition(), promotedPiece);
                }
                //Not a pawn, so just add it like a normal piece.
                else {
                    gameBoard.addPiece(move.getEndPosition(),tempPiece);
                }

                if (tempPiece.pieceColor == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                }
                else {
                    setTeamTurn(TeamColor.WHITE);
                }
                return;
            }
        }
        throw new InvalidMoveException("Your move " + move + " is not a legal move.");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //Rewrite using the updated functions
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        Collection<ChessMove> opposingMoves = getOpposingMoves(teamColor);
        for (ChessMove move : opposingMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && hasValidMoves(teamColor);
    }

    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && hasValidMoves(teamColor);
    }

    public void setBoard(ChessBoard board) {
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                gameBoard.addPiece(tempPosition,tempPiece);
            }
        }
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece != null &&
                        piece.getPieceType() == ChessPiece.PieceType.KING &&
                        piece.getTeamColor() == teamColor) {
                    return pos;
                }
            }
        }
        return null;
    }

    private Collection<ChessMove> getOpposingMoves(TeamColor teamColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    moves.addAll(piece.pieceMoves(gameBoard, pos));
                }
            }
        }
        return moves;
    }

    private boolean hasValidMoves(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(pos);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }
}
