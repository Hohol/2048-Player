package game2048.evaluators;

import game2048.BestMoveFinder;
import game2048.Move;

public abstract class AbstractEvaluator implements Evaluator {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    protected boolean blocked(int x, int y, int[][] board) {
        for (Move move : Move.ALL) {
            int tox = x + move.dx;
            int toy = y + move.dy;
            if (!BestMoveFinder.inside(tox, toy, board.length, board[0].length)) {
                continue;
            }
            if (board[x][y] >= board[tox][toy]) {
                return false;
            }
        }
        return true;
    }
}
