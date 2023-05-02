package mathexpression;

public class TriFunctElement implements TermElement {
    public static enum Type {
        SIN,
        COS,
    }

    /** The type of trigonometric function */
    private final Type type;

    /** Expression in math form inside this trigonometric function */
    private final MathExpre expr;

    public TriFunctElement(Type type, MathExpre expr) {
        this.expr = expr;
        this.type = type;
    }

    public TriFunctElement(sentence.TriFunct.Type type, MathExpre expr) {
        if (type == sentence.TriFunct.Type.SIN) {
            this.type = Type.SIN;
        } else {
            this.type = Type.COS;
        }
        this.expr = expr;
    }

    Type getType() {
        return type;
    }

    MathExpre getExpr() {
        return expr;
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        TriFunctElement other = (TriFunctElement)o;
        return this.type == other.type && this.expr.equals(other.expr);
    }

    @Override public int hashCode() {
        return type.hashCode() * 301 + expr.hashCode();
    }
    
    @Override public String toString() {
        if (type == Type.SIN) {
            if (expr.isEmpty()) {
                return "";
            }
            return "sin(" + expr + ")";
        } else {
            if (expr.isEmpty()) {
                return "1";
            }
            return "cos(" + expr + ")";
        }
    }

    public String toSimpleString(boolean conservative) {
        if (type == Type.SIN) {
            if (expr.isEmpty()) {
                return "";
            }
            return "sin(" + expr.toSimpleString(true) + ")";
        } else {
            if (expr.isEmpty()) {
                return "1";
            }
            return "cos(" + expr.toSimpleString(true) + ")";
        }
    }
    
}
