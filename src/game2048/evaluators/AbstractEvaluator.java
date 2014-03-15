package game2048.evaluators;

import game2048.DefaultBestMoveFinder;
import game2048.Move;

public abstract class AbstractEvaluator implements Evaluator {
    protected static int dist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    protected boolean blocked(int x, int y, int[][] board) {
        for (Move move : Move.ALL) {
            int tox = x + move.dx;
            int toy = y + move.dy;
            if (!DefaultBestMoveFinder.inside(tox, toy, board.length, board[0].length)) {
                continue;
            }
            if (board[x][y] >= board[tox][toy]) {
                return false;
            }
        }
        return true;
    }
}
