package game2048.evaluators;

import game2048.evaluators.Evaluator;

import java.util.Arrays;
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

    public static EvaluatorCombination combinationOfTwo(Evaluator first, Evaluator second, double firstFactor) {
        double secondFactor = 1 - firstFactor;
        return new EvaluatorCombination(Arrays.asList(first, second), Arrays.asList(firstFactor, secondFactor));
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
