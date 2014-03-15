package game2048;

public interface Evaluator {
    double evaluate(int[][] board);
    double getFailCost();
}
