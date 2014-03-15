package game2048.evaluators;

public class FeeForBlockedEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    if (blocked(i, j, board)) {
                        r++;
                    }
                }
            }
        }
        return r;
    }
}
