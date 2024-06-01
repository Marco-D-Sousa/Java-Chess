package application.game;

import application.boardgame.ChessBoard;
import application.boardgame.Piece;
import application.boardgame.Position;
import application.pieces.King;

public class ChessGame {

    private ChessBoard board;
    private boolean whiteTurn = true;

    public ChessGame() {
        this.board = new ChessBoard();
    }

    public boolean makeMove(Position start, Position end) {

        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());

        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            // Execute the move
            board.movePiece(start, end);
            whiteTurn = !whiteTurn; // Change turns
            return true;
        }
        return false;
    }

    public boolean isInCheck(PieceColor kingColor) {

        Position kingPosition = findKingPosition(kingColor);

        for (int row = 0; row < board.getBoard().length; row++) {
            for (int col = 0; col < board.getBoard()[row].length; col++){
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor){
                    if (piece.isValidMove(kingPosition, board.getBoard())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++){
            for (int col = 0; col < board.getBoard()[row].length; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        throw new RuntimeException("King not found");
    }
}
