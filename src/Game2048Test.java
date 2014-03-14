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
        Game2048.MoveAndCost moveAndCost = Game2048.makeBestAction(board, 1);
        assertEquals(moveAndCost.move, "<");
    }

    @Test
    void testBug() {
        int[][] board = {{4, 2, 2}};
        int[][] actual = Game2048.makeMove(board, 0, 1);
        int[][] expected = {{0, 4, 4}};
        compare(actual, expected);
    }

    private void compare(int[][] actual, int[][] expected) {
        boolean equal = Game2048.equals(actual, expected);
        assertTrue("\n" + Game2048.boardToString(actual) + Game2048.boardToString(expected), equal);
    }
}
