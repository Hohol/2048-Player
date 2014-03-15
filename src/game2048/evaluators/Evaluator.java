package game2048.evaluators;

public interface Evaluator {
    double evaluate(int[][] board);
    double getFailCost();
}
