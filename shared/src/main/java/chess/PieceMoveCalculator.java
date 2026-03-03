package chess;

import java.util.ArrayList;
import java.util.List;

public class PieceMoveCalculator {

    public List<ChessMove> calculateMoves(ChessPosition startPosition, ChessBoard board, ChessPiece piece) {
        final ArrayList<ChessMove> moveList = new ArrayList<>();
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            slidingMoves(startPosition, board, piece, 1, 1, moveList);
            slidingMoves(startPosition, board, piece, 1, -1, moveList);
            slidingMoves(startPosition, board, piece, -1, 1, moveList);
            slidingMoves(startPosition, board, piece, -1, -1, moveList);
            return moveList;
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
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
                else if (adjacentPiece.getTeamColor() != piece.getTeamColor()) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
            }
            return moveList;
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
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
                else if (adjacentPiece.getTeamColor() != piece.getTeamColor()) {
                    moveList.add(new ChessMove(startPosition, newPosition, null));
                }
            }
            return moveList;
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                // WHITE pawns move UP (row increases: 2 -> 3 -> 4...)

                // Move forward one square
                if (startPosition.getRow() < 8) {
                    ChessPosition oneForward = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
                    ChessPiece pieceAhead = board.getPiece(oneForward);

                    if (pieceAhead == null) {  // Square must be empty
                        if (startPosition.getRow() + 1 == 8) {  // Promotion
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.KNIGHT));
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

                // Captures (diagonal)
                if (startPosition.getRow() < 8) {
                    // Left diagonal
                    if (startPosition.getColumn() > 1) {
                        ChessPosition leftDiag = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
                        ChessPiece targetPiece = board.getPiece(leftDiag);
                        if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                            if (startPosition.getRow() + 1 == 8) {  // Promotion capture
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.QUEEN));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.ROOK));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.BISHOP));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.KNIGHT));
                            } else {
                                moveList.add(new ChessMove(startPosition, leftDiag, null));
                            }
                        }
                    }

                    // Right diagonal
                    if (startPosition.getColumn() < 8) {
                        ChessPosition rightDiag = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
                        ChessPiece targetPiece = board.getPiece(rightDiag);
                        if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                            if (startPosition.getRow() + 1 == 8) {  // Promotion capture
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.QUEEN));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.ROOK));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.BISHOP));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.KNIGHT));
                            } else {
                                moveList.add(new ChessMove(startPosition, rightDiag, null));
                            }
                        }
                    }
                }
            }
            else {  // BLACK pawns
                // Move forward one square (row decreases: 7 -> 6 -> 5...)
                if (startPosition.getRow() > 1) {
                    ChessPosition oneForward = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
                    ChessPiece pieceAhead = board.getPiece(oneForward);

                    if (pieceAhead == null) {  // Square must be empty
                        if (startPosition.getRow() - 1 == 1) {  // Promotion
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.QUEEN));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.ROOK));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.BISHOP));
                            moveList.add(new ChessMove(startPosition, oneForward, ChessPiece.PieceType.KNIGHT));
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

                // Captures (diagonal)
                if (startPosition.getRow() > 1) {
                    // Left diagonal
                    if (startPosition.getColumn() > 1) {
                        ChessPosition leftDiag = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
                        ChessPiece targetPiece = board.getPiece(leftDiag);
                        if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                            if (startPosition.getRow() - 1 == 1) {  // Promotion capture
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.QUEEN));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.ROOK));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.BISHOP));
                                moveList.add(new ChessMove(startPosition, leftDiag, ChessPiece.PieceType.KNIGHT));
                            } else {
                                moveList.add(new ChessMove(startPosition, leftDiag, null));
                            }
                        }
                    }

                    // Right diagonal
                    if (startPosition.getColumn() < 8) {
                        ChessPosition rightDiag = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
                        ChessPiece targetPiece = board.getPiece(rightDiag);
                        if (targetPiece != null && targetPiece.getTeamColor() != piece.getTeamColor()) {
                            if (startPosition.getRow() - 1 == 1) {  // Promotion capture
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.QUEEN));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.ROOK));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.BISHOP));
                                moveList.add(new ChessMove(startPosition, rightDiag, ChessPiece.PieceType.KNIGHT));
                            } else {
                                moveList.add(new ChessMove(startPosition, rightDiag, null));
                            }
                        }
                    }
                }
            }
            return moveList;
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            //Bishop Moves
            slidingMoves(startPosition, board, piece, 1, 1, moveList);
            slidingMoves(startPosition, board, piece, 1, -1, moveList);
            slidingMoves(startPosition, board, piece, -1, 1, moveList);
            slidingMoves(startPosition, board, piece, -1, -1, moveList);

            //Rook Moves
            slidingMoves(startPosition, board, piece, 1, 0, moveList);
            slidingMoves(startPosition, board, piece, -1, 0, moveList);
            slidingMoves(startPosition, board, piece, 0, 1, moveList);
            slidingMoves(startPosition, board, piece, 0, -1, moveList);

            return moveList;
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            slidingMoves(startPosition, board, piece, 1, 0, moveList);
            slidingMoves(startPosition, board, piece, -1, 0, moveList);
            slidingMoves(startPosition, board, piece, 0, 1, moveList);
            slidingMoves(startPosition, board, piece, 0, -1, moveList);
            return moveList;
        }
        else {
            throw new RuntimeException("INVALID PIECE! WHAT DID YOU DO?!?!");
        }
    }

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
}