package game2048.evaluators;

public class MinimizeDuplicatedMaxEvaluator extends AbstractEvaluator {
    static int cnt[] = new int[1 << 20];

    @Override
    public double evaluate(int[][] board) {
        int max = 0;
        int diffCnt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                max = Math.max(max, board[i][j]);
                if (cnt[board[i][j]] == 0) {
                    diffCnt++;
                }
                cnt[board[i][j]]++;
            }
        }
        int target = -1;
        int order = 0;
        for (int i = max; i >= 2; i >>= 1) {
            if (cnt[i] >= 2) {
                target = i;
                break;
            }
            if(cnt[i] > 0) {
                order++;
            }
        }
        for (int i = max; i >= 2; i >>= 1) {
            cnt[i] = 0;
        }
        if (target == -1) {
            return 0;
        }
        return diffCnt - order;
    }
}