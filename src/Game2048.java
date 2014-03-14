import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game2048 {

    public static final int N = 4;
    public static final int[] dx = {0, 1, 0, -1};
    public static final int[] dy = {1, 0, -1, 0};
    public static final String[] moves = {">", "v", "<", "^"};

    private static int MAX_DEPTH = 3;
    private static long IMAGE_SIMILARITY_THRESHOLD = 200000;

    static class MoveAndCost {
        String move;
        int cost;

        MoveAndCost(String move, int cost) {
            this.move = move;
            this.cost = cost;
        }
    }

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {
        initImages();

        int[][] board = null;
        int[][] oldBoard = null;
        while (true) {
            int[][] newBoard = readBoard();
            validate(board, oldBoard, newBoard);
            board = newBoard;
            oldBoard = copyBoard(board);
            String move = makeBestAction(board, 0).move; //it changes board
            System.out.println("\nMove = " + move + "\n");
            pressKey(move);
        }
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] r = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            r[i] = board[i].clone();
        }
        return r;
    }

    private static void validate(int[][] board, int[][] oldBoard, int[][] newBoard) {
        if(!valid(board, oldBoard, newBoard)) {
            System.out.println("Fayol");
            System.out.println("Old:");
            print(oldBoard);
            System.out.println("Expected:");
            print(board);
            System.out.println("Actual:");
            print(newBoard);
            throw new RuntimeException();
        }
    }

    private static boolean valid(int[][] board, int[][] oldBoard, int[][] newBoard) {
        if(board == null || oldBoard == null) {
            return true;
        }
        if(equals(oldBoard, newBoard)) {
            return true;
        }
        int differentCnt = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != newBoard[i][j]) {
                    if(board[i][j] == 0 && (newBoard[i][j] == 2 || newBoard[i][j] == 4)) {
                        differentCnt++;
                    } else {
                        return false;
                    }
                }
            }
        }
        return differentCnt <= 1;
    }

    private static void pressKey(String move) throws Throwable {
        Robot robot = new Robot();
        if (move.equals(">")) {
            robot.keyPress(KeyEvent.VK_RIGHT);
        } else if (move.equals("<")) {
            robot.keyPress(KeyEvent.VK_LEFT);
        } else if (move.equals("v")) {
            robot.keyPress(KeyEvent.VK_DOWN);
        } else {
            robot.keyPress(KeyEvent.VK_UP);
        }
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
                BufferedImage image = robot.createScreenCapture(new Rectangle(
                        startX + a * j + indent * j, startY + a * i + indent * i,
                        a, a
                ));
                board[i][j] = getNumber(image);
            }
        }
        print(board);
        return board;
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

    private static int getNumber(BufferedImage image) throws IOException, InterruptedException {
        for (int i = 0; i < images.size(); i++) {
            if (similar(image, images.get(i))) {
                return numbers.get(i);
            }
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

    static MoveAndCost makeBestAction(int[][] board, int depth) {
        int n = board.length;
        int m = board[0].length;
        int minCost = Integer.MAX_VALUE;
        int[][] bestBoard = null;
        String bestMove = null;
        for (int dir = 0; dir < 4; dir++) {
            int[][] newBoard = makeMove(board, dx[dir], dy[dir]);
            if (equals(board, newBoard)) {
                continue;
            }
            int cost = 0;
            if (depth == MAX_DEPTH) {
                cost = evaluate(newBoard);
            } else {
                int emptyCnt = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (newBoard[i][j] == 0) {
                            emptyCnt++;
                            newBoard[i][j] = 2;
                            cost += makeBestAction(newBoard, depth + 1).cost * 0.9;
                            newBoard[i][j] = 4;
                            cost += makeBestAction(newBoard, depth + 1).cost * 0.1;
                            newBoard[i][j] = 0;
                        }
                    }
                }
                cost /= emptyCnt;
            }
            if (cost < minCost) {
                minCost = cost;
                bestMove = moves[dir];
                bestBoard = newBoard;
            }
        }
        if (bestBoard == null) {
            return new MoveAndCost(">", 1000);
        }
        if (depth == 0) {
            for (int i = 0; i < board.length; i++) {
                System.arraycopy(bestBoard[i], 0, board[i], 0, board[i].length);
            }
        }
        return new MoveAndCost(bestMove, minCost);
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

    static int[][] makeMove(int[][] board, int dx, int dy) {
        int n = board.length;
        int m = board[0].length;
        int[][] r = new int[n][];
        for (int i = 0; i < n; i++) {
            r[i] = board[i].clone();
        }
        if (dx == 1 || dy == 1) {
            for (int i = n - 1; i >= 0; i--) {
                for (int j = m - 1; j >= 0; j--) {
                    move(r, i, j, dx, dy, n, m);
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    move(r, i, j, dx, dy, n, m);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(r[i][j] < 0) {
                    r[i][j] *= -1;
                }
            }
        }
        return r;
    }

    private static boolean inside(int x, int y, int n, int m) {
        return x >= 0 && y >= 0 && x < n && y < m;
    }

    private static void move(int[][] board, int x, int y, int dx, int dy, int n, int m) {
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

    private static int evaluate(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != 0) {
                    r++;
                }
            }
        }
        r += maxPositionFee(board);
        r += blockedFee(board);

        return r;
    }

    private static int blockedFee(int[][] board) {
        int r = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                if (blocked(i, j, board)) {
                    r++;
                }
            }
        }
        return r * 3;
    }

    private static boolean blocked(int x, int y, int[][] board) {
        for (int dir = 0; dir < 4; dir++) {
            int tox = x + dx[dir];
            int toy = y + dy[dir];
            if (!inside(tox, toy, board.length, board[0].length)) {
                continue;
            }
            if (board[x][y] >= board[tox][toy]) {
                return false;
            }
        }
        return true;
    }

    private static int maxPositionFee(int[][] board) {
        int x = -1, y = -1;
        int max = -1;
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] > max) {
                    max = board[i][j];
                    x = i;
                    y = j;
                }
            }
        }
        int sx = -1, sy = -1;
        int smax = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] != max && board[i][j] > smax) {
                    smax = board[i][j];
                    sx = i;
                    sy = j;
                }
            }
        }
        return Math.min(
                Math.min(dist(x, y, 0, 0), dist(x, y, n - 1, 0)),
                Math.min(dist(x, y, 0, m - 1), dist(x, y, n - 1, m - 1))
        ) * 2 + dist(x, y, sx, sy);
    }

    private static int dist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
