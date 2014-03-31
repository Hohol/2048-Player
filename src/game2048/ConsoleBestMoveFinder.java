package game2048;

import game2048.evaluators.Evaluator;

import java.util.Scanner;

public class ConsoleBestMoveFinder extends BestMoveFinder {
    Scanner in = new Scanner(System.in);

    public ConsoleBestMoveFinder(Evaluator evaluator) {
        super(evaluator);
    }

    @Override
    Move findBestMove(int[][] board) {
        while(true) {
            String s = in.next();
            for (Move move : Move.ALL) {
                if(move.arrow.equals(s)) {
                    int[][] newBoard = makeMove(board, move);
                    copyBoard(board, newBoard);
                    return move;
                }
            }
        }
    }
}
