package chess;

import java.util.ArrayList;
import java.util.List;

public class PieceMoveCalculator {

    public List<ChessMove> calculateMoves(ChessPosition startPosition, ChessBoard board, ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case BISHOP -> bishopMoves(startPosition, board, piece);
            case KING -> stepMoves(startPosition, board, piece, kingOffsets());
            case KNIGHT -> stepMoves(startPosition, board, piece, knightOffsets());
            case PAWN -> pawnMoves(startPosition, board, piece);
            case QUEEN -> queenMoves(startPosition, board, piece);
            case ROOK -> rookMoves(startPosition, board, piece);
        };
    }

    // Slides in a direction until hitting a wall or piece
    private void slidingMoves(ChessPosition start, ChessBoard board, ChessPiece piece,
                              int rowDir, int colDir, List<ChessMove> moveList) {
        for (int i = 1; i < 8; i++) {
            int newRow = start.getRow() + i * rowDir;
            int newCol = start.getColumn() + i * colDir;
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                break;
            }
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece target = board.getPiece(newPos);
            if (target == null) {
                moveList.add(new ChessMove(start, newPos, null));
            } else if (target.getTeamColor() != piece.getTeamColor()) {
                moveList.add(new ChessMove(start, newPos, null));
                break;
            } else {
                break;
            }
        }
    }

    private List<ChessMove> bishopMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        List<ChessMove> moves = new ArrayList<>();
        slidingMoves(start, board, piece, 1, 1, moves);
        slidingMoves(start, board, piece, 1, -1, moves);
        slidingMoves(start, board, piece, -1, 1, moves);
        slidingMoves(start, board, piece, -1, -1, moves);
        return moves;
    }

    private List<ChessMove> rookMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        List<ChessMove> moves = new ArrayList<>();
        slidingMoves(start, board, piece, 1, 0, moves);
        slidingMoves(start, board, piece, -1, 0, moves);
        slidingMoves(start, board, piece, 0, 1, moves);
        slidingMoves(start, board, piece, 0, -1, moves);
        return moves;
    }

    private List<ChessMove> queenMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        List<ChessMove> moves = new ArrayList<>();
        moves.addAll(bishopMoves(start, board, piece));
        moves.addAll(rookMoves(start, board, piece));
        return moves;
    }

    // Handles king and knight — pieces that jump to fixed offsets
    private List<ChessMove> stepMoves(ChessPosition start, ChessBoard board, ChessPiece piece,
                                      List<int[]> offsets) {
        List<ChessMove> moves = new ArrayList<>();
        for (int[] offset : offsets) {
            int newRow = start.getRow() + offset[0];
            int newCol = start.getColumn() + offset[1];
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece target = board.getPiece(newPos);
            if (target == null || target.getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(start, newPos, null));
            }
        }
        return moves;
    }

    private List<int[]> kingOffsets() {
        return List.of(new int[]{1,0}, new int[]{-1,0}, new int[]{0,1}, new int[]{0,-1},
                new int[]{1,1}, new int[]{1,-1}, new int[]{-1,1}, new int[]{-1,-1});
    }

    private List<int[]> knightOffsets() {
        return List.of(new int[]{2,1}, new int[]{1,2}, new int[]{2,-1}, new int[]{1,-2},
                new int[]{-2,1}, new int[]{-1,2}, new int[]{-2,-1}, new int[]{-1,-2});
    }

    private List<ChessMove> pawnMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return whitePawnMoves(start, board, piece);
        } else {
            return blackPawnMoves(start, board, piece);
        }
    }

    private void addPawnAdvance(ChessPosition start, ChessPosition target, int promotionRow,
                                List<ChessMove> moves) {
        if (target.getRow() == promotionRow) {
            moves.add(new ChessMove(start, target, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(start, target, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(start, target, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(start, target, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, target, null));
        }
    }

    private List<ChessMove> whitePawnMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        List<ChessMove> moves = new ArrayList<>();
        int row = start.getRow();
        int col = start.getColumn();

        // Move forward
        if (row < 8) {
            ChessPosition oneForward = new ChessPosition(row + 1, col);
            if (board.getPiece(oneForward) == null) {
                addPawnAdvance(start, oneForward, 8, moves);
                // Double move from start
                if (row == 2) {
                    ChessPosition twoForward = new ChessPosition(row + 2, col);
                    if (board.getPiece(twoForward) == null) {
                        moves.add(new ChessMove(start, twoForward, null));
                    }
                }
            }
        }
        // Capture diagonally
        if (row < 8 && col > 1) {
            ChessPosition leftCapture = new ChessPosition(row + 1, col - 1);
            ChessPiece target = board.getPiece(leftCapture);
            if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                addPawnAdvance(start, leftCapture, 8, moves);
            }
        }
        if (row < 8 && col < 8) {
            ChessPosition rightCapture = new ChessPosition(row + 1, col + 1);
            ChessPiece target = board.getPiece(rightCapture);
            if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                addPawnAdvance(start, rightCapture, 8, moves);
            }
        }
        return moves;
    }

    private List<ChessMove> blackPawnMoves(ChessPosition start, ChessBoard board, ChessPiece piece) {
        List<ChessMove> moves = new ArrayList<>();
        int row = start.getRow();
        int col = start.getColumn();

        // Move forward (black moves down)
        if (row > 1) {
            ChessPosition oneForward = new ChessPosition(row - 1, col);
            if (board.getPiece(oneForward) == null) {
                addPawnAdvance(start, oneForward, 1, moves);
                // Double move from start
                if (row == 7) {
                    ChessPosition twoForward = new ChessPosition(row - 2, col);
                    if (board.getPiece(twoForward) == null) {
                        moves.add(new ChessMove(start, twoForward, null));
                    }
                }
            }
        }
        // Capture diagonally
        if (row > 1 && col > 1) {
            ChessPosition leftCapture = new ChessPosition(row - 1, col - 1);
            ChessPiece target = board.getPiece(leftCapture);
            if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                addPawnAdvance(start, leftCapture, 1, moves);
            }
        }
        if (row > 1 && col < 8) {
            ChessPosition rightCapture = new ChessPosition(row - 1, col + 1);
            ChessPiece target = board.getPiece(rightCapture);
            if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                addPawnAdvance(start, rightCapture, 1, moves);
            }
        }
        return moves;
    }
}