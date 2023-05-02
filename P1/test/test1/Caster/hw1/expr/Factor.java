package expr;

import polynomial.Polynomial;

public interface Factor {
    public default Polynomial ergodic() {
        return null;
    }
}