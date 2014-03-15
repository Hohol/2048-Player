package game2048.evaluators;

public class OverrideFailCostEvaluator implements Evaluator {
    private final Evaluator delegate;
    private final double failCost;

    public OverrideFailCostEvaluator(Evaluator delegate, double failCost) {
        this.delegate = delegate;
        this.failCost = failCost;
    }

    @Override
    public double evaluate(int[][] board) {
        return delegate.evaluate(board);
    }

    @Override
    public double getFailCost() {
        return failCost;
    }

    @Override
    public String toString() {
        return "OverrideFailCostEvaluator with {" + delegate + "} with failCost = " + failCost;
    }
}
