package game2048.evaluators;

public class MonotonicRowEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int r1 = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length-1; j++) {
                if(board[i][j] < board[i][j+1]) {
                    r1++;
                }
            }
        }
        return r1;
        /*int r2 = 0;
        for (int i = 0; i < board.length-1; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] < board[i+1][j]) {
                    r2++;
                }
            }
        }
        return Math.min(r1, r2);/**/
    }
}
