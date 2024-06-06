package application;

import application.boardgame.ChessBoard;
import application.boardgame.Piece;
import application.game.ChessGame;
import application.game.PieceColor;
import application.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ChessGameGUI extends JFrame {

    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final ChessGame game = new ChessGame();
    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {{
        put(Pawn.class, "\u265F");
        put(Rook.class, "\u265C");
        put(Knight.class, "\u265E");
        put(Bishop.class, "\u265D");
        put(Queen.class, "\u265B");
        put(King.class, "\u265A");
    }};

    public ChessGameGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        pack(); // Adjust window size to fit the chessboard
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                squares[row][col] = square;
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        ChessBoard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col); //Assuming Chessboard has getPiece method
                if (piece != null)  {
                    String symbol = pieceUnicodeMap.get(piece.getClass());//If use unicode symbols
                    Color color = (piece.getColor() == PieceColor.WHITE) ? Color.WHITE : Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);//Or, if updating with icons or any other graphical representation
                } else {
                    squares[row][col].clearPieceSymbol();//Ensure this method clears square
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if (game.handleSquareSelection(row, col)) {
            refreshBoard();
            checkGameState();
        }
    }

    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor(); //This method should return the current player's color
        boolean inCheck = game.isInCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    public static void main(String [] args) {
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}