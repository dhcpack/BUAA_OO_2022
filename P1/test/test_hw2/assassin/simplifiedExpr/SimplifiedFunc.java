package simplifiedexpr;

import java.util.Objects;

public class SimplifiedFunc implements SimplifiedBase {
    private final String funcName;
    private final SimplifiedExpr simplifiedExpr;

    public SimplifiedFunc(String funcName, SimplifiedExpr simplifiedExpr) {
        this.funcName = funcName;
        this.simplifiedExpr = simplifiedExpr;
    }

    public SimplifiedFunc(SimplifiedFunc simplifiedFunc) {
        funcName = new String(simplifiedFunc.funcName);
        simplifiedExpr = new SimplifiedExpr(simplifiedFunc.simplifiedExpr);
    }

    public String getFuncName() {
        return funcName;
    }

    public SimplifiedExpr getSimplifiedExpr() {
        return simplifiedExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedFunc that = (SimplifiedFunc) o;
        return Objects.equals(funcName, that.funcName) &&
                Objects.equals(simplifiedExpr, that.simplifiedExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, simplifiedExpr);
    }

    public String toString() {
        return funcName + "(" + simplifiedExpr + ")";
    }
}
