package game2048.bestmovefinders;

import game2048.Move;
import game2048.OriginalGamePlayer;
import game2048.evaluators.Evaluator;

public class MinimaxBestMoveFinder extends BestMoveFinder {
    private final int maxCallCnt;

    public MinimaxBestMoveFinder(Evaluator evaluator, int maxCallCnt) {
        super(evaluator);
        this.maxCallCnt = maxCallCnt;
    }

    @Override
    public Move findBestMove(int[][] board) {
        return findBestMoveInternal(board, maxCallCnt).move;
    }

    private MoveAndCost findBestMoveInternal(int[][] board, int allowedCallCnt) {
        allowedCallCnt--; //this call
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
            double cost = getCost(nextLevelCalls, newBoard);
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

    private double getCost(int nextLevelCalls, int[][] newBoard) {
        double cost = Double.NEGATIVE_INFINITY;
        if (nextLevelCalls == 0) {
            cost = evaluator.evaluate(newBoard);
        } else {
            for (int i = 0; i < newBoard.length; i++) {
                for (int j = 0; j < newBoard[i].length; j++) {
                    if (newBoard[i][j] == 0) {
                        newBoard[i][j] = 2;
                        cost = Math.max(cost, findBestMoveInternal(newBoard, nextLevelCalls).cost);
                        newBoard[i][j] = 4;
                        cost = Math.max(cost, findBestMoveInternal(newBoard, nextLevelCalls).cost);
                        newBoard[i][j] = 0;
                    }
                }
            }
        }
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
            if (!OriginalGamePlayer.equals(board, newBoard)) {
                r[dir] = newBoard;
            }
        }
        return r;
    }
}
