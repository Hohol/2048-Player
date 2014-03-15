package game2048;

public abstract class AbstractEvaluator implements Evaluator {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
