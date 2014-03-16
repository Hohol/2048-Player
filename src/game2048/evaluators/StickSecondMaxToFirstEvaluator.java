package game2048.evaluators;

public class StickSecondMaxToFirstEvaluator extends AbstractEvaluator{
    @Override
    public double evaluate(int[][] board) {
        int max = 0, secondMax = 0;
        int maxX = -1, maxY = -1, sMaxX = -1, sMaxY = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] > max) {
                    secondMax = max;
                    sMaxX = maxX;
                    sMaxY = maxY;

                    max = board[i][j];
                    maxX = i;
                    maxY = j;
                }
            }
        }
        if(secondMax == 0) {
            return 0;
        }
        return dist(maxX, maxY, sMaxX, sMaxY);
    }
}
