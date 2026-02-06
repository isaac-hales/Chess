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
        gameBoard.resetBoard(); //Not a mistake. It should set up the default position, by default.
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
        //Checking if the piece is in check / checkmate, and if so, it will be removed.
        potentialMoves.removeIf(move -> isInCheck(tempPiece.pieceColor) || isInCheckmate(tempPiece.pieceColor));
        return potentialMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece tempPiece = gameBoard.getPiece(move.getStartPosition());
        if (tempPiece == null){return;}
        final ArrayList<ChessMove> potentialMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (tempPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not currently " + tempPiece.getTeamColor()+"s turn yet.");
        }
        for (ChessMove legalMove: potentialMoves){
            if (legalMove.equals(move)){
                gameBoard.addPiece(move.getStartPosition(),null);
                gameBoard.addPiece(move.getEndPosition(),tempPiece);
                if (tempPiece.pieceColor == TeamColor.WHITE){setTeamTurn(TeamColor.BLACK);}
                else{setTeamTurn(TeamColor.WHITE);}
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
        //Find the king for the color listed, check all the valid moves for the opposite team
        //If one of them equals the king position, then the piece is in check.
        ChessPosition kingPosition = null;
        ArrayList<ChessMove> opposingMoves = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                //If the position is null, then skip, so that we don't crash the system
                if (gameBoard.getPiece(tempPosition) == null){continue;}
                //Checking if it's a king piece and the proper color
                if (gameBoard.getPiece(tempPosition).getPieceType() == ChessPiece.PieceType.KING &&
                        gameBoard.getPiece(tempPosition).getTeamColor() == teamColor) {
                    kingPosition = tempPosition;
                }
                else if (gameBoard.getPiece(tempPosition).getTeamColor() != teamColor) {
                    ChessPiece tempPiece = gameBoard.getPiece(tempPosition);
                    opposingMoves = (ArrayList<ChessMove>) tempPiece.pieceMoves(gameBoard,tempPosition);
                }
            }
        }
        for (ChessMove legalMove: opposingMoves){
            if (legalMove.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //if not in check, then it can't be in checkmate
        if (!isInCheck(teamColor)){
            return false;
        }

        ChessPosition kingPosition = kingFinder(teamColor);
        PieceMoveCalculator kingMoveList = new PieceMoveCalculator();
        kingMoveList.calculateMoves(kingPosition,gameBoard, gameBoard.getPiece(kingPosition));
        //Check all the moves in kingMoveList, they are not all in opposing team valid moves,
        //then return true or false based on the result


        return true;
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
        //Whill check the valid move list. If the list is empty, then it this should return true, and end the game.
    }

    /**
     * Gets the king position for isInCheck, isInCheckmate, and isInStalemate.
     * @param teamColor
     * @return the position of the king
     */
    public ChessPosition kingFinder(TeamColor teamColor){
        ChessPosition kingPosition = null;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                //If the position is null, then skip, so that we don't crash the system
                if (gameBoard.getPiece(tempPosition) == null){continue;}
                //Checking if it's a king piece and the proper color
                if (gameBoard.getPiece(tempPosition).getPieceType() == ChessPiece.PieceType.KING &&
                        gameBoard.getPiece(tempPosition).getTeamColor() == teamColor) {
                    kingPosition = tempPosition;
                    break;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Gets the moves of the opposing team for isInCheckmate and isInStalemate.
     * @param teamColor
     *
     * @return opposingMoves
     */
    public Collection<ChessMove> opposingTeamMoves(TeamColor teamColor){
        ArrayList<ChessMove> opposingMoves = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition tempPosition = new ChessPosition(i,j);
                //If the position is null, then skip, so that we don't crash the system
                if (gameBoard.getPiece(tempPosition) == null){continue;}
                //Checking if it's a king piece and the proper color
                else if (gameBoard.getPiece(tempPosition).getTeamColor() != teamColor) {
                    ChessPiece tempPiece = gameBoard.getPiece(tempPosition);
                    opposingMoves = (ArrayList<ChessMove>) tempPiece.pieceMoves(gameBoard,tempPosition);
                }
            }
        }
        return opposingMoves;
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
