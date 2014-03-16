package game2048.evaluators;

public class StickTwoBigEvaluator extends AbstractEvaluator {
    static int cnt[] = new int[1 << 20];

    @Override
    public double evaluate(int[][] board) {
        int max = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                max = Math.max(max, board[i][j]);
                cnt[board[i][j]]++;
            }
        }
        int target = -1;
        for (int i = max; i >= 2; i >>= 1) {
            if (cnt[i] >= 2) {
                target = i;
                break;
            }
        }

        int fx = -1, fy = -1, sx = -1, sy = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == target) {
                    if (fx == -1) {
                        fx = i;
                        fy = j;
                    } else {
                        sx = i;
                        sy = j;
                    }
                }
            }
        }
        if(sx == -1) {
            return 0;
        }
        return dist(fx, fy, sx, sy);
    }

}
