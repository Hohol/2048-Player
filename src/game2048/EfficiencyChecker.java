package game2048;

import java.util.Random;

public class EfficiencyChecker {
    private final Random rnd = new Random();
    private final int MAX_STEP_CNT = 1000;
    public double checkEfficiency(BestMoveFinder bestMoveFinder, int n, int m) {
        double sum = 0;
        int cnt = 0;
        for (int i = 0; i < MAX_STEP_CNT; i++) {
            double curValue = playGame(bestMoveFinder, n, m);
            sum += curValue;
            cnt++;
            double avgEfficiency = sum / cnt;
            //printCurStats(curValue, bestMoveFinder.getMaxDepth(), avgEfficiency);
        }
        double avgEfficiency = sum / cnt;

        printResultReport(bestMoveFinder, avgEfficiency);
        return avgEfficiency;
    }

    private void printResultReport(BestMoveFinder bestMoveFinder, double avgEfficiency) {
        System.out.println(bestMoveFinder);
        System.out.println("Avg efficiency = " + avgEfficiency);
        System.out.println();
    }

    private void printCurStats(double curValue, int maxDepth, double avgEfficiency) {
        System.out.println("cur efficiency = " + curValue);
        System.out.println("average efficiency (with maxDepth " + maxDepth + ") = " + avgEfficiency);
    }

    private double playGame(BestMoveFinder bestMoveFinder, int n, int m) {
        int[][] board = new int[n][m];
        addRandomTile(board);
        addRandomTile(board);
        do {
            /*System.out.println("After spawn:");
            Game2048.print(board);/**/
            Move move = bestMoveFinder.findBestMove(board);
            /*System.out.println("After move: " + move);
            Game2048.print(board);/**/
        } while(addRandomTile(board));
        int sum = 0;
        for (int[] aBoard : board) {
            for (int anABoard : aBoard) {
                sum += anABoard;
            }
        }
        return sum;
    }

    private boolean addRandomTile(int[][] board) {
        int emptyCnt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) {
                    emptyCnt++;
                }
            }
        }
        if(emptyCnt == 0) {
            return false;
        }
        int pos = rnd.nextInt(emptyCnt);
        int curPos = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 0) {
                    if(curPos == pos) {
                        if(rnd.nextDouble() < 0.9) {
                            board[i][j] = 2;
                        } else {
                            board[i][j] = 4;
                        }
                        return true;
                    }
                    curPos++;
                }
            }
        }
        throw new RuntimeException();
    }
}
