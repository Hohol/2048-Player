package game2048;

//average efficiency = 566.7412538088251
public class TileCntEvaluator extends AbstractEvaluator {

    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != 0) {
                    r++;
                }
            }
        }
        return r;
    }

    @Override
    public double getFailCost() {
        return 1000;
    }
}
