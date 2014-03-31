package game2048;

import game2048.evaluators.Evaluator;

public class DefaultBestMoveFinder extends BestMoveFinder {

    public int getMaxDepth() {
        return maxDepth;
    }

    private final int maxDepth;

    public DefaultBestMoveFinder(Evaluator evaluator, int maxDepth) {
        super(evaluator);
        this.maxDepth = maxDepth;
    }

    @Override
    Move findBestMove(int[][] board) {
        return findBestMoveInternal(board, 0).move;
    }

    private MoveAndCost findBestMoveInternal(int[][] board, int depth) {
        int n = board.length;
        int m = board[0].length;
        double minCost = Double.POSITIVE_INFINITY;
        int[][] bestBoard = null;
        Move bestMove = null;
        for (Move move : Move.ALL) {
            int[][] newBoard = makeMove(board, move);
            if (Game2048.equals(board, newBoard)) {
                continue;
            }
            double cost = 0;
            if (depth == maxDepth) {
                cost = evaluator.evaluate(newBoard);
            } else {
                int emptyCnt = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (newBoard[i][j] == 0) {
                            emptyCnt++;
                            newBoard[i][j] = 2;
                            cost += findBestMoveInternal(newBoard, depth + 1).cost * 0.9;
                            newBoard[i][j] = 4;
                            cost += findBestMoveInternal(newBoard, depth + 1).cost * 0.1;
                            newBoard[i][j] = 0;
                        }
                    }
                }
                cost /= emptyCnt;
            }
            if (cost < minCost) {
                minCost = cost;
                bestMove = move;
                bestBoard = newBoard;
            }
        }
        if (bestBoard == null) {
            return new MoveAndCost(null, evaluator.getFailCost());
        }
        if (depth == 0) {
            copyBoard(board, bestBoard);
        }
        return new MoveAndCost(bestMove, minCost);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " with {" + evaluator.toString() + "} and maxDepth = " + maxDepth;
    }
}
