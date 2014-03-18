package game2048;

import game2048.evaluators.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Game2048 {

    public static final int N = 4;

    private static long IMAGE_SIMILARITY_THRESHOLD = 300000;

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {

        play(new FixedStateCountBestMoveFinder(
                EvaluatorCombination.combinationOfTwo(
                        new SnakeShapeEvaluator(), new MonotonicRowEvaluator(), 0.5), 400000)
        );/**/

        checkEfficiency();
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
            efficiencyChecker.checkEfficiency(new FixedStateCountBestMoveFinder(evaluator, 5000), N, N);
        }

    }

    private static void play(BestMoveFinder bestMoveFinder) throws Throwable {
        initImages();
        int[][] board = null;
        int[][] oldBoard = null;
        while (true) {
            int[][] newBoard = null;
            for (int i = 0; i < 5; i++) {
                newBoard = readBoard();
                if (validate(board, oldBoard, newBoard)) {
                    break;
                }
            }
            board = newBoard;
            oldBoard = copyBoard(board);
            Move move = bestMoveFinder.findBestMove(board);
            System.out.println("\nMove = " + move + "\n");
            pressKey(move);
        }
    }

    static int[][] copyBoard(int[][] board) {
        int[][] r = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            r[i] = board[i].clone();
        }
        return r;
    }

    private static boolean validate(int[][] board, int[][] oldBoard, int[][] newBoard) {
        if (!valid(board, oldBoard, newBoard)) {
            System.out.println("Fayol");
            System.out.println("Old:");
            print(oldBoard);
            System.out.println("Expected:");
            print(board);
            System.out.println("Actual:");
            print(newBoard);
            return false;
            //throw new RuntimeException();
        }
        return true;
    }

    private static boolean valid(int[][] board, int[][] oldBoard, int[][] newBoard) {
        if (board == null || oldBoard == null) {
            return true;
        }
        if (equals(oldBoard, newBoard)) {
            return true;
        }
        int differentCnt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != newBoard[i][j]) {
                    if (board[i][j] == 0 && (newBoard[i][j] == 2 || newBoard[i][j] == 4)) {
                        differentCnt++;
                    } else {
                        return false;
                    }
                }
            }
        }
        return differentCnt <= 1;
    }

    private static void pressKey(Move move) throws Throwable {
        Robot robot = new Robot();
        robot.keyPress(move.keyCode);
        robot.delay(310);
    }

    static java.util.List<Integer> numbers = new ArrayList<Integer>();
    static java.util.List<BufferedImage> images = new ArrayList<BufferedImage>();

    private static int[][] readBoard() throws Throwable {
        Robot robot = new Robot();
        int startX = 17, startY = 83;
        int a = 106;
        int indent = 137 - 123 + 1;


        int[][] board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int number = getNumber(robot, startX, startY, a, indent, i, j);
                board[i][j] = number;
            }
        }
        print(board);
        return board;
    }

    private static int getNumber(Robot robot, int startX, int startY, int a, int indent, int i, int j) throws IOException, InterruptedException {
        for (int t = 0; t < 5; t++) {
            BufferedImage image = getImage(robot, startX, startY, a, indent, i, j);
            int r = getNumber(image, true);
            if (r != -1) {
                return r;
            }
        }
        BufferedImage image = getImage(robot, startX, startY, a, indent, i, j);
        return getNumber(image, false);
    }

    private static BufferedImage getImage(Robot robot, int startX, int startY, int a, int indent, int i, int j) {
        return robot.createScreenCapture(new Rectangle(
                startX + a * j + indent * j, startY + a * i + indent * i,
                a, a
        ));
    }

    private static void initImages() {
        for (int i = 0; ; i = (i == 0 ? 2 : i * 2)) {
            String name = Integer.toString(i) + ".png";
            try {
                BufferedImage image = ImageIO.read(new File(name));
                numbers.add(i);
                images.add(image);
            } catch (Exception e) {
                break;
            }
        }
    }

    private static int getNumber(BufferedImage image, boolean retryIfUnknown) throws IOException, InterruptedException {
        for (int i = 0; i < images.size(); i++) {
            if (similar(image, images.get(i))) {
                return numbers.get(i);
            }
        }
        if (retryIfUnknown) {
            return -1;
        }
        ImageIO.write(image, "png", new File("question.png"));
        System.out.println("What is this number?");
        int r = in.nextInt();
        numbers.add(r);
        ImageIO.write(image, "png", new File(Integer.toString(r) + ".png"));
        images.add(image);
        return r;
    }

    private static boolean similar(BufferedImage imageA, BufferedImage imageB) {
        double sum = similarity(imageA, imageB);
        return sum < IMAGE_SIMILARITY_THRESHOLD;
    }

    private static double similarity(BufferedImage imageA, BufferedImage imageB) {
        double sum = 0;
        for (int i = 0; i < imageA.getWidth(); i++) {
            for (int j = 0; j < imageA.getHeight(); j++) {
                int colourA = imageA.getRGB(i, j);

                int redA = (colourA & 0x00ff0000) >> 16;
                int greenA = (colourA & 0x0000ff00) >> 8;
                int blueA = colourA & 0x000000ff;

                int colourB = imageB.getRGB(i, j);

                int redB = (colourB & 0x00ff0000) >> 16;
                int greenB = (colourB & 0x0000ff00) >> 8;
                int blueB = colourB & 0x000000ff;

                long diff = Math.abs(redA - redB) + Math.abs(greenA - greenB) + Math.abs(blueA - blueB);
                sum += diff;
            }
        }
        return sum;
    }

    static void print(int[][] board) {
        System.out.println(boardToString(board));
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


    static boolean equals(int[][] actual, int[][] expected) {
        boolean equal = true;
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                if (expected[i][j] != actual[i][j]) {
                    equal = false;
                }
            }
        }
        return equal;
    }

    public static int evaluate(int[][] board) {
        int r = 0;
        //r += distToCornerFee(board);
        //r += tilesCnt(board);
        //r += maxPositionFee(board);
        //r += blockedFee(board);
        //r += orderFee(board);

        return r;
    }

    /*private static int distToCornerFee(int[][] board) {
        int r = 0;
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                r += Math.min(
                        Math.min(dist(i, j, 0, 0), dist(i, j, n - 1, 0)),
                        Math.min(dist(i, j, 0, m - 1), dist(i, j, n - 1, m - 1))
                ) * board[i][j];
            }
        }
        return r;
    }

    private static int tilesCnt(int[][] board) {
        int r = 0;
        for (int[] aBoard : board) {
            for (int anABoard : aBoard) {
                if (anABoard != 0) {
                    r++;
                }
            }
        }
        return r;
    }

    static int[] orderStat = new int[100000];
    static boolean[] used = new boolean[100000];

    private static int orderFee(int[][] board) {
        int n = board.length;
        int m = board[0].length;
        int max = 0;
        int r = 0;
        for (int i = 0; i < board.length; i++) {

            for (int j = 0; j < m; j++) {
                if (board[i][j] != 0) {
                    max = Math.max(max, board[i][j]);
                    used[board[i][j]] = true;
                }
            }
        }
        int curOrder = 0;
        for (int i = max; i >= 2; i >>= 1) {
            if (used[i]) {
                orderStat[i] = curOrder;
                curOrder++;
            }
        }
        int maxOrder = curOrder - 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] != 0) {
                    int order = orderStat[board[i][j]];
                    int x = order / m;
                    int y;
                    if (x % 2 == 0) {
                        y = order % m;
                    } else {
                        y = (m - order % m - 1);
                    }
                    int thisCellOrder = i * m;
                    if (x % 2 == 0) {
                        thisCellOrder += j;
                    } else {
                        thisCellOrder += (m - j - 1);
                    }
                    if (order <= thisCellOrder) {
                        r += dist(i, j, x, y) * (maxOrder - order + 1);
                    } else {
                        r += (order - thisCellOrder) * 100;
                    }
                }
            }
        }
        for (int i = max; i >= 2; i >>= 1) {
            used[i] = false;
        }
        return r;
    }/**/


}
