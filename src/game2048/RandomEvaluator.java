package game2048;

import java.util.Random;

//average efficiency (with maxDepth 0) = 264.7315762273902
public class RandomEvaluator implements Evaluator {
    private Random rnd = new Random();
    @Override
    public double evaluate(int[][] board) {
        return rnd.nextDouble();
    }

    @Override
    public double failCost() {
        return 1000;
    }
}