package game2048;

public class BestMoveFinder { //todo MinimaxBestMoveFinder
    private final Evaluator evaluator;

    public int getMaxDepth() {
        return maxDepth;
    }

    private final int maxDepth;

    public BestMoveFinder(Evaluator evaluator, int maxDepth) {
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

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
            for (int i = 0; i < board.length; i++) {
                System.arraycopy(bestBoard[i], 0, board[i], 0, board[i].length);
            }
        }
        return new MoveAndCost(bestMove, minCost);
    }

    static int[][] makeMove(int[][] board, Move move) {
        int n = board.length;
        int m = board[0].length;
        int[][] r = new int[n][];
        for (int i = 0; i < n; i++) {
            r[i] = board[i].clone();
        }
        if (move.dx == 1 || move.dy == 1) {
            for (int i = n - 1; i >= 0; i--) {
                for (int j = m - 1; j >= 0; j--) {
                    move(r, i, j, move.dx, move.dy, n, m);
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    move(r, i, j, move.dx, move.dy, n, m);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (r[i][j] < 0) {
                    r[i][j] *= -1;
                }
            }
        }
        return r;
    }

    static boolean inside(int x, int y, int n, int m) {
        return x >= 0 && y >= 0 && x < n && y < m;
    }

    static private void move(int[][] board, int x, int y, int dx, int dy, int n, int m) {
        while (true) {
            if (board[x][y] == 0) {
                break;
            }
            int tox = x + dx;
            int toy = y + dy;
            if (!inside(tox, toy, n, m)) {
                break;
            }
            if (board[tox][toy] == 0) {
                board[tox][toy] = board[x][y];
                board[x][y] = 0;
                x = tox;
                y = toy;
            } else if (board[tox][toy] == board[x][y]) {
                board[tox][toy] = -board[x][y] * 2;
                board[x][y] = 0;
                break;
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "BestMoveFinder with {" + evaluator.toString() + "} and maxDepth = " + maxDepth;
    }

    static class MoveAndCost {
        Move move;
        double cost;

        MoveAndCost(Move move, double cost) {
            this.move = move;
            this.cost = cost;
        }
    }
}
