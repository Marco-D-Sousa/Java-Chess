package application;

import java.util.Scanner;
import java.util.InputMismatchException;

import application.game.ChessException;
import application.game.ChessMatch;
import application.game.ChessPiece;
import application.game.ChessPosition;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while (true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}

// TODO Bishop Piece https://hackr.io/blog/how-to-build-a-java-chess-game-app