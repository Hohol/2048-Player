package game2048.evaluators;

public class OrderFeeEvaluator extends AbstractEvaluator{

    static int[] orderStat = new int[100000];
    static boolean[] used = new boolean[100000];

    @Override
    public double evaluate(int[][] board) {
        int n = board.length;
        int m = board[0].length;
        int max = 0;
        int r = 0;
        for (int i = 0; i < board.length; i++) {

            for (int j = 0; j < m; j++) {
                if (board[i][j] != 0) {
                    max = Math.max(max, board[i][j]);
                    used[board[i][j]] = true;
                }
            }
        }
        int curOrder = 0;
        for (int i = max; i >= 2; i >>= 1) {
            if (used[i]) {
                orderStat[i] = curOrder;
                curOrder++;
            }
        }
        int maxOrder = curOrder - 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] != 0) {
                    int order = orderStat[board[i][j]];
                    int x = order / m;
                    int y;
                    if (x % 2 == 0) {
                        y = order % m;
                    } else {
                        y = (m - order % m - 1);
                    }
                    int thisCellOrder = i * m;
                    if (x % 2 == 0) {
                        thisCellOrder += j;
                    } else {
                        thisCellOrder += (m - j - 1);
                    }
                    if(thisCellOrder >= order) {
                        r += dist(i, j, x, y) * (maxOrder - order + 1);
                    } else {
                        r += 100;
                    }
                }
            }
        }
        for (int i = max; i >= 2; i >>= 1) {
            used[i] = false;
        }
        return r;
    }
}
