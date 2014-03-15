package game2048;

//average efficiency = 952.1654033567949
public class TileCntPlusBlockedEvaluator implements Evaluator{
    @Override
    public double evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != 0) {
                    r++;
                    if (blocked(i, j, board)) {
                        r++;
                    }
                }
            }
        }
        return r;
    }

    private static boolean blocked(int x, int y, int[][] board) {
        for (Move move : Move.ALL) {
            int tox = x + move.dx;
            int toy = y + move.dy;
            if (!BestMoveFinder.inside(tox, toy, board.length, board[0].length)) {
                continue;
            }
            if (board[x][y] >= board[tox][toy]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double failCost() {
        return 1000;
    }
}
