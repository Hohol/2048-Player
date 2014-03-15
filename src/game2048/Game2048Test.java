package game2048;

import game2048.evaluators.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Test
public class Game2048Test {
    @Test
    void test() {
        int[][] board = {{1, 1}};
        int[][] actual = DefaultBestMoveFinder.makeMove(board, Move.LEFT);
        int[][] expected = {{2, 0}};
        compare(actual, expected);
    }

    @Test
    void testZero() {
        int[][] board = {{0, 0}};
        int[][] actual = DefaultBestMoveFinder.makeMove(board, Move.LEFT);
        int[][] expected = {{0, 0}};
        compare(actual, expected);
    }

    @Test
    void testMoveRight() {
        int[][] board = {{1, 2, 0, 0}};
        int[][] actual = DefaultBestMoveFinder.makeMove(board, Move.RIGHT);
        int[][] expected = {{0, 0, 1, 2}};
        compare(actual, expected);
    }

    @Test
    void test2() {
        int[][] board = {{2}, {0}, {1}, {1}};
        int[][] actual = DefaultBestMoveFinder.makeMove(board, Move.UP);
        int[][] expected = {{2}, {2}, {0}, {0}};
        compare(actual, expected);
    }

    @Test
    void testFourCanSpawn() {
        int[][] board = {{1, 1},
                {64, 4}};
        Move move = new DefaultBestMoveFinder(new TileCntEvaluator(), 0).findBestMove(board);
        assertEquals(move, Move.LEFT);
    }

    @Test
    void testBug() {
        int[][] board = {{4, 2, 2}};
        int[][] actual = DefaultBestMoveFinder.makeMove(board, Move.RIGHT);
        int[][] expected = {{0, 4, 4}};
        compare(actual, expected);
    }

    @Test
    void testBestMoveFinder() {
        int[][] board = {{8, 2}, {2, 0}, {0, 2}};
        Move move = new DefaultBestMoveFinder(new TileCntEvaluator(), 0).findBestMove(board);
        assertEquals(move, Move.UP);
    }

    @Test
    void combinationTest() {
        int[][] board =
                {
                        {64, 128, 32, 8},
                        {32, 16, 8, 2},
                        {16, 8, 0, 0},
                        {4, 2, 0, 0}
                };
        Evaluator combination = EvaluatorCombination.combinationOfTwo(
                new TileCntEvaluator(), new FeeForBlockedEvaluator(), //must be same as TileCntPlusBlockedEvaluator
                0.4
        );
        Evaluator simple = new TileCntPlusBlockedEvaluator();
        int[][] boardBuf = Game2048.copyBoard(board);
        assertEquals(new DefaultBestMoveFinder(combination, 0).findBestMove(board),
                new DefaultBestMoveFinder(simple, 0).findBestMove(boardBuf));
    }

    /*@Test
    void testEvaluation() {
        int[][] badPosition =
                {{1024, 512, 256, 2},
                        {0, 0, 0, 128}
                };
        int[][] goodPosition =
                {{1024, 512, 256, 2},
                        {0, 0, 128, 0}
                };

        compareBadAndGoodPositions(badPosition, goodPosition);
    }

    @Test
    void testEvaluation2() {
        int[][] badPosition =
                {{2, 2, 2, 0},
                        {1024, 0, 0, 0}
                };
        int[][] goodPosition =
                {{1024, 2, 2, 2},
                        {0, 0, 0, 0}};

        compareBadAndGoodPositions(badPosition, goodPosition);
    }

    @Test
    void testEvaluation3() {
        int[][] badPosition =
                {{2, 4},
                 {0, 0}};
        int[][] goodPosition =
                {{0, 4},
                 {2, 0}};

        compareBadAndGoodPositions(badPosition, goodPosition);
    }/**/

    private void compareBadAndGoodPositions(int[][] badPosition, int[][] goodPosition) {
        int badCost = Game2048.evaluate(badPosition);
        int goodCost = Game2048.evaluate(goodPosition);
        assertTrue("\nbadCost = " + badCost + "\ngoodCost = " + goodCost, badCost > goodCost);
    }

    private void compare(int[][] actual, int[][] expected) {
        boolean equal = Game2048.equals(actual, expected);
        assertTrue("\n" + Game2048.boardToString(actual) + Game2048.boardToString(expected), equal);
    }
}
