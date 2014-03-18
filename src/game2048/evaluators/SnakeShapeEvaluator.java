package game2048.evaluators;

public class SnakeShapeEvaluator extends AbstractEvaluator {
    static int[] power = new int[1 << 20]; static {
        for (int p = 1; p < 20; p++) {
            power[1 << p] = p;
        }
    }

    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        int n = board.length;
        int m = board[0].length;
        int x = 0, y = 0;
        while (y != m) {
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
            if (board[x][y] != 0 && board[nextX][nextY] != 0 && board[x][y] >= board[nextX][nextY]) {
                r -= board[x][y];
            } else {
                break;
            }
            x = nextX;
            y = nextY;
        }
        return r;/**/
        /*int r = 0;
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {

                if(nextJ == m) {
                    continue;
                }
                if (board[i][j] < board[nextI][nextJ]) {
                    r++;
                }
            }
        }
        return r;/**/
    }

    /*@Override
    public double getFailCost() {
        return 1 << 20;
    }/**/
}
