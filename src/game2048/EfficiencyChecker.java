package game2048;

public class EfficiencyChecker {
    private final int MAX_STEP_CNT = 10000;

    public double checkEfficiency(BestMoveFinder bestMoveFinder, int n, int m, boolean shouldLog) {
        double sum = 0;
        int cnt = 0;
        for (int i = 0; i < MAX_STEP_CNT; i++) {
            double curValue = playGame(bestMoveFinder, n, m, shouldLog);
            sum += curValue;
            cnt++;
            double avgEfficiency = sum / cnt;
            printCurStats(curValue, bestMoveFinder, avgEfficiency);
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

    private void printCurStats(double curValue, BestMoveFinder bestMoveFinder, double avgEfficiency) {
        System.out.println("cur efficiency = " + curValue);
        System.out.println("average efficiency (with " + bestMoveFinder + ") = " + avgEfficiency);
    }

    private double playGame(BestMoveFinder bestMoveFinder, int n, int m, boolean shouldLog) {
        return new AutoPlayer().playFromStart(bestMoveFinder, n, m, shouldLog);
    }
}
