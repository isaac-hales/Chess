package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard gameBoard = new ChessBoard();
    TeamColor teamTurn; 
    //int turnCounter;

    //This is a constructor, so any function that we need to run at the start, we run here.
    public ChessGame() {
        gameBoard.resetBoard(); //Is this a mistake, in case we want to set up other boards?
        teamTurn = TeamColor.WHITE; //First turn belongs to white team.
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece tempPiece = gameBoard.getPiece(startPosition);
        if (tempPiece == null){
            return null;
        }

        final ArrayList<ChessMove> potentialMoves = (ArrayList<ChessMove>) tempPiece.pieceMoves(gameBoard,startPosition);
        for (ChessMove move : potentialMoves){
            //Checking if the piece is in check / checkmate
            if (isInCheck(tempPiece.pieceColor) || isInCheckmate(tempPiece.pieceColor)){
                potentialMoves.remove(move);
            }
        }
        return potentialMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
        // The computer will just input a move in the parameter.
        //Run isInCheck and isInCheckmate on own color to check for legality
        }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //Get the movelist of all pieces of opposing color, and then see if any of the moves include the teamColor king piece
        //So run validMoves, and then check if any of the pieces is
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //Similar code to Checkmate function, only difference is that this code returns true if the king is NOT in danger, but
        //surrounding squares are.
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                gameBoard.addPiece(tempPosition,tempPiece);
            }
        }
        throw new RuntimeException("Not implemented");

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }


}
