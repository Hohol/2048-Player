package game2048;

import game2048.bestmovefinders.BestMoveFinder;
import game2048.bestmovefinders.FixedStateCountBestMoveFinder;
import game2048.evaluators.*;

import java.util.*;
import java.util.List;

public class Main {

    static EvaluatorCombination currentBestEvaluator = new EvaluatorCombination(
            Arrays.asList((Evaluator) new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), new TileCntEvaluator()),
            Arrays.asList(1.0, 1.0, 0.1)
    );

    public static void main(String[] args) throws Throwable {

        new OriginalGamePlayer().play(new FixedStateCountBestMoveFinder(
                currentBestEvaluator
                , 400000)
        );/**/

        //checkEfficiency();

        //autoplay();
    }

    private static void autoplay() {
        int maxCallCnt = 400000;
        //BestMoveFinder bestMoveFinder = new ConsoleBestMoveFinder(currentBestEvaluator);
        EvaluatorCombination evaluator = new EvaluatorCombination(
                Arrays.asList((Evaluator) new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), new TileCntEvaluator(), new TestEvaluator()),
                Arrays.asList(1.0, 1.0, 0.1, 1.0)
        );
        /*System.out.println(evaluator.evaluate(stringToBoard(
                " 4096     4     2     4 \n" +
                " 2048     8     4     2 \n" +
                " 1024    64     8     4 \n" +
                "  128     0     4     0")));
        System.out.println(evaluator.evaluate(stringToBoard(
                " 4096     2     0     0 \n" +
                " 2048     8     4     0 \n" +
                " 1024     16     2     0\n" +
                "  128    64     8 0")));
        if(true) {
            return;
        }/**/
        BestMoveFinder bestMoveFinder =
                new FixedStateCountBestMoveFinder(
                        evaluator
                , maxCallCnt);/**/
        int[][] board = {
                { 4096 ,    0   ,  2  ,   0},
                {2048   ,  4    , 4  ,   4},
                {1024  ,   8 ,    8  ,   2},
                {128  ,  64 ,    4  ,   4}
        };
        //323 - good test
        //new AutoPlayer(323333).playFromGivenState(bestMoveFinder, board, true);
        Random rnd = new Random();
        int seed = rnd.nextInt();
        System.out.println("Seed = " + seed);
        new AutoPlayer(seed).playFromStart(bestMoveFinder, OriginalGamePlayer.N, OriginalGamePlayer.N, true);
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
