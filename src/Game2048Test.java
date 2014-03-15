import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@Test
public class Game2048Test {
    @Test
    void test() {
        int[][] board = {{1, 1}};
        int[][] actual = Game2048.makeMove(board, 0, -1);
        int[][] expected = {{2, 0}};
        compare(actual, expected);
    }

    @Test
    void testZero() {
        int[][] board = {{0, 0}};
        int[][] actual = Game2048.makeMove(board, 0, -1);
        int[][] expected = {{0, 0}};
        compare(actual, expected);
    }

    @Test
    void testMoveRight() {
        int[][] board = {{1, 2, 0, 0}};
        int[][] actual = Game2048.makeMove(board, 0, 1);
        int[][] expected = {{0, 0, 1, 2}};
        compare(actual, expected);
    }

    @Test
    void test2() {
        int[][] board = {{2}, {0}, {1}, {1}};
        int[][] actual = Game2048.makeMove(board, -1, 0);
        int[][] expected = {{2}, {2}, {0}, {0}};
        compare(actual, expected);
    }

    @Test
    void testFourCanSpawn() {
        int[][] board = {{1, 1},
                {64, 4}};
        Game2048.MoveAndCost moveAndCost = Game2048.makeBestAction(board, 0, 1, new TileCntEvaluator());
        assertEquals(moveAndCost.move, "<");
    }

    @Test
    void testBug() {
        int[][] board = {{4, 2, 2}};
        int[][] actual = Game2048.makeMove(board, 0, 1);
        int[][] expected = {{0, 4, 4}};
        compare(actual, expected);
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
