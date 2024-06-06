package application.boardgame;

import application.game.PieceColor;

public abstract class Piece {

    protected Position position;
    protected PieceColor color;

    public Piece(PieceColor color, Position position) {
        this.position = position;
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isValidMove(Position newPosition, Piece[][] board);
}
