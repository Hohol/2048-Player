package game2048;

import java.util.List;

public class EvaluatorCombination implements Evaluator {
    private final List<Evaluator> evaluators;
    private final List<Double> factors;
    private final int n;
    private final double failCost;

    public EvaluatorCombination(List<Evaluator> evaluators, List<Double> factors) {
        if (evaluators.size() != factors.size()) {
            throw new RuntimeException();
        }
        this.evaluators = evaluators;
        this.factors = factors;
        n = evaluators.size();
        failCost = failCostInternal();
    }

    @Override
    public double evaluate(int[][] board) {
        double r = 0;
        for (int i = 0; i < n; i++) {
            r += evaluators.get(i).evaluate(board) * factors.get(i);
        }
        return r;
    }

    @Override
    public double getFailCost() {
        return failCost;
    }

    private double failCostInternal() {
        double r = 0;
        for (int i = 0; i < n; i++) {
            r += evaluators.get(i).getFailCost() * factors.get(i);
        }
        return r;
    }

    @Override
    public String toString() {
        return "Combination of evaluators " + evaluators + " with factors " + factors;
    }
}
