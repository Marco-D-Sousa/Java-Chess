package application.game;

import application.boardgame.BoardException;

public class ChessException extends BoardException {

    public ChessException(String msg) {
        super(msg);
    }
}
