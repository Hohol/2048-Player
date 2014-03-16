package game2048.evaluators;

public class SnakeShapeEvaluator extends AbstractEvaluator {
    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                int nextI, nextJ;
                if (j % 2 == 0) {
                    if (i == n - 1) {
                        nextI = i;
                        nextJ = j + 1;
                    } else {
                        nextI = i + 1;
                        nextJ = j;
                    }
                } else {
                    if (i == 0) {
                        nextI = i;
                        nextJ = j + 1;
                    } else {
                        nextI = i - 1;
                        nextJ = j;
                    }
                }
                if(nextJ == m) {
                    continue;
                }
                if (board[i][j] < board[nextI][nextJ]) {
                    r++;
                }
            }
        }
        return r;
    }
}
