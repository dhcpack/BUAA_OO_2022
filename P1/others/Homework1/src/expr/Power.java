package expr;

public class Power implements Factor {
    private final int degree;

    public Power(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }
}
