package game2048.evaluators;

public class TransposeEvaluator implements Evaluator {

    private final Evaluator evaluator;

    public TransposeEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public double evaluate(int[][] board) {
        double r = evaluator.evaluate(board);
        transpose(board);
        r = Math.min(r, evaluator.evaluate(board));
        transpose(board);
        return r;
    }

    private void transpose(int[][] board) {
        if (board.length != board[0].length) {
            throw new UnsupportedOperationException();
        }
        int n = board.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int buf = board[i][j];
                board[i][j] = board[j][i];
                board[j][i] = buf;
            }
        }
    }

    @Override
    public double getFailCost() {
        return evaluator.getFailCost();
    }
}
