package game2048;

import game2048.evaluators.Evaluator;

public abstract class BestMoveFinder {
    protected final Evaluator evaluator;

    public BestMoveFinder(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public static boolean inside(int x, int y, int n, int m) {
        return x >= 0 && y >= 0 && x < n && y < m;
    }

    static void move(int[][] board, int x, int y, int dx, int dy, int n, int m) {
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

    abstract Move findBestMove(int[][] board);

    static class MoveAndCost {
        Move move;
        double cost;

        MoveAndCost(Move move, double cost) {
            this.move = move;
            this.cost = cost;
        }
    }
}
