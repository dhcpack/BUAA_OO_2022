package mathexpression;

public class VarElement extends sentence.Variable implements TermElement {
    public VarElement(String varName) {
        super(varName);
    }

    public String toSimpleString(boolean conservative) {
        return this.toString();
    }
}