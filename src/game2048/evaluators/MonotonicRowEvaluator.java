package game2048.evaluators;

public class MonotonicRowEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length-1; j++) {
                if(board[i][j] < board[i][j+1]) {
                    r++;
                }
            }
        }
        return r;
    }
}
