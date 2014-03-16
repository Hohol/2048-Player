package game2048.evaluators;

public class StickMaxToLeftCornerEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int x = -1, y = -1;
        int max = -1;
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] > max) {
                    max = board[i][j];
                    x = i;
                    y = j;
                }
            }
        }

        return Math.min(x, n - 1 - x);
    }

    private int distToNearestCorner(int x, int y, int n, int m) {
        return Math.min(x, n - x - 1) + Math.min(y, m - y - 1);
    }
}
