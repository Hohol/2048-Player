package game2048.evaluators;

import game2048.BestMoveFinder;
import game2048.Move;

//Avg efficiency = 973.5904
public class TileCntPlusBlockedEvaluator extends AbstractEvaluator {
    @Override
    public double evaluate(int[][] board) {
        int tileCnt = 0;
        int blockedCnt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    tileCnt++;
                    if (blocked(i, j, board)) {
                        blockedCnt++;
                    }
                }
            }
        }

        return tileCnt * 0.4 + blockedCnt * 0.6;
    }

    @Override
    public double getFailCost() {
        return 1000;
    }
}
