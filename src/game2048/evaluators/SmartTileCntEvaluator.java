package game2048.evaluators;

public class SmartTileCntEvaluator extends AbstractEvaluator {
    static double[] cost = new double[1<<20];
    static {
        for(int p = 1; p <= 15; p++) {
            cost[1<<p] = Math.sqrt(p);
        }
    }
    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != 0) {
                    if(board[i][j] >= 32) {
                        r += 10;
                    } else {
                        r++;
                    }
                }
            }
        }
        return r;
    }
}
