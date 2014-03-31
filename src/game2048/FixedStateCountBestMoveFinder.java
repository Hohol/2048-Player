package game2048;

import game2048.evaluators.Evaluator;

public class FixedStateCountBestMoveFinder extends BestMoveFinder {
    private final int maxCallCnt;

    public FixedStateCountBestMoveFinder(Evaluator evaluator, int maxCallCnt) {
        super(evaluator);
        this.maxCallCnt = maxCallCnt;
    }

    @Override
    Move findBestMove(int[][] board) {
        return findBestMoveInternal(board, maxCallCnt).move;
    }

    private MoveAndCost findBestMoveInternal(int[][] board, int allowedCallCnt) {
        allowedCallCnt--; //this call
        int n = board.length;
        int m = board[0].length;
        double minCost = Double.POSITIVE_INFINITY;
        int[][] bestBoard = null;
        Move bestMove = null;

        int[][][] newBoards = getNewBoards(board);
        int needCalls = getNeedCallsForNextDepth(newBoards);
        if(needCalls == 0) {
            return new MoveAndCost(null, evaluator.getFailCost());
        }
        int nextLevelCalls = allowedCallCnt / needCalls; //todo proper rounding?
        for (int dir = 0; dir < 4; dir++) {
            int[][] newBoard = newBoards[dir];
            if(newBoard == null) {
                continue;
            }
            double cost = 0;
            if (nextLevelCalls == 0) {
                cost = evaluator.evaluate(newBoard);
            } else {
                cost = getCost(n, m, nextLevelCalls, newBoard, cost);
            }
            if (cost < minCost) {
                minCost = cost;
                bestMove = Move.ALL[dir];
                bestBoard = newBoard;
            }
        }
        if (allowedCallCnt+1 == maxCallCnt) {
            copyBoard(board, bestBoard);
        }
        return new MoveAndCost(bestMove, minCost);
    }

    private double getCost(int n, int m, int nextLevelCalls, int[][] newBoard, double cost) {
        int emptyCnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (newBoard[i][j] == 0) {
                    emptyCnt++;
                    newBoard[i][j] = 2;
                    cost += findBestMoveInternal(newBoard, nextLevelCalls).cost * 0.9;
                    newBoard[i][j] = 4;
                    cost += findBestMoveInternal(newBoard, nextLevelCalls).cost * 0.1;
                    newBoard[i][j] = 0;
                }
            }
        }
        cost /= emptyCnt;
        return cost;
    }

    private int getNeedCallsForNextDepth(int[][][] newBoards) {
        int r = 0;
        for (int[][] board : newBoards) {
            if (board == null) {
                continue;
            }
            for (int[] row : board) {
                for (int val : row) {
                    if (val == 0) {
                        r += 2;
                    }
                }
            }
        }
        return r;
    }

    private int[][][] getNewBoards(int[][] board) {
        int[][][] r = new int[4][][];
        for (int dir = 0; dir < 4; dir++) {
            Move move = Move.ALL[dir];
            int[][] newBoard = makeMove(board, move);
            if (!Game2048.equals(board, newBoard)) {
                r[dir] = newBoard;
            }
        }
        return r;
    }

    private int findNeededCallsForNextLevel(int[][] board) {
        int r = 0;
        for (Move move : Move.ALL) {
            int[][] newBoard = makeMove(board, move);
            if (Game2048.equals(board, newBoard)) {
                continue;
            }
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (newBoard[i][j] == 0) {
                        r += 2;  // can put 2 or 4
                    }
                }
            }
        }
        return r;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " with {" + evaluator.toString() + "} with maxCallCnt = " + maxCallCnt;
    }
}
