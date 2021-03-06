package game2048;

import game2048.bestmovefinders.*;
import game2048.evaluators.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Test
public class Game2048Test {
    @Test
    void test() {
        int[][] board = {{1, 1}};
        int[][] actual = BestMoveFinder.makeMove(board, Move.LEFT);
        int[][] expected = {{2, 0}};
        compare(actual, expected);
    }

    @Test
    void testZero() {
        int[][] board = {{0, 0}};
        int[][] actual = BestMoveFinder.makeMove(board, Move.LEFT);
        int[][] expected = {{0, 0}};
        compare(actual, expected);
    }

    @Test
    void testMoveRight() {
        int[][] board = {{1, 2, 0, 0}};
        int[][] actual = BestMoveFinder.makeMove(board, Move.RIGHT);
        int[][] expected = {{0, 0, 1, 2}};
        compare(actual, expected);
    }

    @Test
    void test2() {
        int[][] board = {{2}, {0}, {1}, {1}};
        int[][] actual = BestMoveFinder.makeMove(board, Move.UP);
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
        int[][] actual = BestMoveFinder.makeMove(board, Move.RIGHT);
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
        int[][] boardBuf = OriginalGamePlayer.copyBoard(board);
        assertEquals(new DefaultBestMoveFinder(combination, 0).findBestMove(board),
                new DefaultBestMoveFinder(simple, 0).findBestMove(boardBuf));
    }

    private void compare(int[][] actual, int[][] expected) {
        boolean equal = OriginalGamePlayer.equals(actual, expected);
        assertTrue("\n" + Main.boardToString(actual) + Main.boardToString(expected), equal);
    }
}
