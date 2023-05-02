package mathexpression;

public interface TermElement {

    @Override public String toString();

    public String toSimpleString(boolean conservative);
    
}
