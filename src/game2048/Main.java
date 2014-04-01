package game2048;

import game2048.bestmovefinders.BestMoveFinder;
import game2048.bestmovefinders.FixedStateCountBestMoveFinder;
import game2048.evaluators.*;

import java.util.*;
import java.util.List;

public class Main {

    static Evaluator currentBestEvaluator = new EvaluatorCombination(
            Arrays.asList((Evaluator) new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), new TileCntEvaluator(), new MinimizeNonSnakePartEvaluator()),
            Arrays.asList(1.0, 1.0, 0.1, 1.0)
    );

    public static void main(String[] args) throws Throwable {

        new OriginalGamePlayer().play(new FixedStateCountBestMoveFinder(
                currentBestEvaluator
                , 400000)
        );/**/

        //checkEfficiency();

        autoplay();
    }

    private static void autoplay() {
        int maxCallCnt = 400000;
        //BestMoveFinder bestMoveFinder = new ConsoleBestMoveFinder(currentBestEvaluator);
        Evaluator evaluator = new EvaluatorCombination(
                Arrays.asList((Evaluator) new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), new TileCntEvaluator(), new MinimizeNonSnakePartEvaluator()),
                Arrays.asList(1.0, 1.0, 0.1, 1.0)
        );/**/
        BestMoveFinder bestMoveFinder =
                new FixedStateCountBestMoveFinder(
                        evaluator
                , maxCallCnt);/**/
        int[][] board = stringToBoard(
                " 8192    64     4     0 \n" +
                " 4096   128    64     2 \n" +
                " 2048   256    16     2 \n" +
                " 1024   512     4     8 "
        );
        //323 - good test
        new AutoPlayer(323333).playFromGivenState(bestMoveFinder, board, true);
        Random rnd = new Random();
        int seed = rnd.nextInt();
        System.out.println("Seed = " + seed);
        //new AutoPlayer(seed).playFromStart(bestMoveFinder, OriginalGamePlayer.N, OriginalGamePlayer.N, true);
    }


    private static void checkEfficiency() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();

        evaluators.add(EvaluatorCombination.combinationOfTwo(
                new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), 0.5)
        );

        //for (double firstFactor = 0; firstFactor < 1.01; firstFactor += 0.1) {
        //double firstFactor = 0.6; {
        //{
            /*EvaluatorCombination combination = EvaluatorCombination.combinationOfTwo(
                    new TileCntEvaluator(), new MonotonicRowEvaluator(),
                    firstFactor
            );
            evaluators.add(combination);
        }/**/

        EfficiencyChecker efficiencyChecker = new EfficiencyChecker();
        int maxDepth = 0;
        for (Evaluator evaluator : evaluators) {
            //efficiencyChecker.checkEfficiency(new DefaultBestMoveFinder(evaluator, maxDepth), N, N);
            efficiencyChecker.checkEfficiency(new FixedStateCountBestMoveFinder(evaluator, 5000), OriginalGamePlayer.N, OriginalGamePlayer.N, false);
        }

    }

    static String boardToString(int[][] board) {
        StringBuilder s = new StringBuilder();
        for (int[] aBoard : board) {
            for (int anABoard : aBoard) {
                s.append(anABoard + "\t");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    static int[][] stringToBoard(String s) {
        int[][] r = new int[OriginalGamePlayer.N][OriginalGamePlayer.N];
        Scanner sin = new Scanner(s);
        for (int i = 0; i < OriginalGamePlayer.N; i++) {
            for (int j = 0; j < OriginalGamePlayer.N; j++) {
                r[i][j] = sin.nextInt();
            }
        }
        return r;
    }
}
