package game2048.evaluators;

import game2048.evaluators.AbstractEvaluator;

import java.util.Random;

//average efficiency (with maxDepth 0) = 264.7315762273902
public class RandomEvaluator extends AbstractEvaluator {
    private Random rnd = new Random();
    @Override
    public double evaluate(int[][] board) {
        return rnd.nextDouble();
    }

    @Override
    public double getFailCost() {
        return 1000;
    }
}
