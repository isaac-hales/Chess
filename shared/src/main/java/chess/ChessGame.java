package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChessGame {
    ChessBoard gameBoard = new ChessBoard();
    TeamColor teamTurn;
    private boolean gameOver = false;

    public ChessGame() { //This is a constructor, so any function that we need to run at the start, we run here.
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
        if (tempPiece == null) {
            return null;
        }
        Collection<ChessMove> potentialMoves = tempPiece.pieceMoves(gameBoard, startPosition);
        ArrayList<ChessMove> legalMoves = new ArrayList<>();
        for (ChessMove move : potentialMoves) {
            ChessPiece capturedPiece = gameBoard.getPiece(move.getEndPosition());

            gameBoard.addPiece(move.getEndPosition(), tempPiece);
            gameBoard.addPiece(move.getStartPosition(), null);

            boolean kingInCheck = isInCheck(tempPiece.getTeamColor());

            gameBoard.addPiece(move.getStartPosition(), tempPiece); //Undoing the move.
            gameBoard.addPiece(move.getEndPosition(), capturedPiece); //Putting the capturedPiece back in its original position.
            if (!kingInCheck) { //Adding it if the king is not in check.
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece tempPiece = gameBoard.getPiece(move.getStartPosition());
        if (tempPiece == null) {
            throw new InvalidMoveException("No piece was selected. ");
        }
        final ArrayList<ChessMove> potentialMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (tempPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not currently " + tempPiece.getTeamColor()+"s turn yet.");
        }
        for (ChessMove legalMove: potentialMoves){
            if (legalMove.equals(move)){
                gameBoard.addPiece(move.getStartPosition(),null);
                if (move.getPromotionPiece() != null){ //Checking if it's a pawn, and then doing promotion if so.
                    ChessPiece promotedPiece = new ChessPiece(tempPiece.getTeamColor(), move.getPromotionPiece());
                    gameBoard.addPiece(move.getEndPosition(), promotedPiece);
                }
                else { //Not a pawn, so just add it like a normal piece.
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
        if (isInCheck(teamColor) && hasNoValidMoves(teamColor)) {
            setGameOver(true);
            return true;
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && hasNoValidMoves(teamColor)){
            setGameOver(true);
            return true;
        }
        return false;
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

    private boolean hasNoValidMoves(TeamColor teamColor) {
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

    public ChessBoard getBoard() {
        return gameBoard;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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
