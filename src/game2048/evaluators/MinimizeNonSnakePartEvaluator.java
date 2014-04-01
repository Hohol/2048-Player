package game2048.evaluators;

public class MinimizeNonSnakePartEvaluator extends AbstractEvaluator {
    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        int n = board.length;
        int m = board[0].length;
        int x = 0, y = 0;
        boolean snakeFinished = false;
        while (y != m) {
            if(snakeFinished) {
                r = Math.max(r, board[x][y]);
            }
            int nextX, nextY;
            if (y % 2 == 0) {
                if (x == n - 1) {
                    nextX = x;
                    nextY = y + 1;
                } else {
                    nextX = x + 1;
                    nextY = y;
                }
            } else {
                if (x == 0) {
                    nextX = x;
                    nextY = y + 1;
                } else {
                    nextX = x - 1;
                    nextY = y;
                }
            }
            if(nextY == m) {
                break;
            }
            if (board[x][y] != 0 && board[nextX][nextY] != 0 && board[x][y] >= board[nextX][nextY]) {
            } else {
                snakeFinished = true;
            }

            x = nextX;
            y = nextY;
        }
        return r;
    }
}
