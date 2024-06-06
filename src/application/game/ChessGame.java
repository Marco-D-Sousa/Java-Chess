package application.game;

import application.boardgame.ChessBoard;
import application.boardgame.Piece;
import application.boardgame.Position;
import application.pieces.King;

public class ChessGame {

    private ChessBoard board;
    private boolean whiteTurn = true; // White starts the game

    public ChessGame() {
        this.board = new ChessBoard();
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void resetGame() {
        this.board = new ChessBoard(); //Re-initialize the board
        this.whiteTurn = true; //Reset turn to white
    }

    public PieceColor getCurrentPlayerColor() {
        return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private Position selectedPosition;

    public boolean isPieceSelected() {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col) {

        if (selectedPosition == null) {
            Piece selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
                selectedPosition = new Position(row, col);
                return false; //Indicate a piece was selected but not moved
            }
        } else {
            //Attempt to move the selected piece
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            selectedPosition = null; //Reset selection regardless of move success
            return moveMade; //Return true if a move was successfully made
        }
        return false; //Return false if no piece was selected or move was not made
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

    public boolean isCheckMate(PieceColor kingColor) {

        if (!isInCheck(kingColor)) {
            return false; // Not in check, so cannot be checkmate
        }

        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        // Attempt to find a move that gets the king out of check
        for (int rowOffSet = -1; rowOffSet <= 1; rowOffSet++){
            for (int colOffSet = -1; colOffSet <= 1; colOffSet++) {
                if (rowOffSet == 0 && colOffSet == 0) {
                    continue; // Skip the current position of the king
                }
                Position newPosition = new Position(kingPosition.getRow() + rowOffSet, kingPosition.getColumn() + colOffSet);
                // Check if moving the king to the new position is a legal move and does not

                // result in a check
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard()) &&
                        !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false; // Found a move that gets the king out of check, so it's not checkmate
                }
            }
        }
        return true; // No legal moves available to escape check, so it's checkmate
    }

    private boolean isPositionOnBoard(Position position) {
        return  position.getRow() >= 0 && position.getRow() < board.getBoard().length && position.getColumn() >= 0 &&
                position.getColumn() < board.getBoard()[0].length;
    }

    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        // Simulate the move temporarily
        Piece temp = board.getPiece(to.getRow(), to.getColumn());
        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
        board.setPiece(from.getRow(), from.getColumn(), null);

        boolean inCheck = isInCheck(kingColor);

        // Undo the move
        board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
        board.setPiece(to.getRow(), to.getColumn(), temp);

        return inCheck;
    }
}
