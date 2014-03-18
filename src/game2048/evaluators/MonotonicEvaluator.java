package game2048.evaluators;

public class MonotonicEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int rCol1 = 0, rCol2 = 0, rRow1 = 0, rRow2 = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (i != board.length - 1) {
                    if (board[i][j] < board[i + 1][j]) {
                        rCol1++;
                    } else if (board[i][j] > board[i + 1][j]) {
                        rCol2++;
                    }
                }
                if (j != board[0].length - 1) {
                    if (board[i][j] < board[i][j + 1]) {
                        rRow1++;
                    } else if (board[i][j] > board[i][j + 1]) {
                        rRow2++;
                    }
                }
            }
        }
        return Math.min(rCol1, rCol2) + Math.min(rRow1, rRow2);
    }
}
