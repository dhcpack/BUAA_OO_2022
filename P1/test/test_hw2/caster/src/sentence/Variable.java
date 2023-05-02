package sentence;

public class Variable {
    private String varName;
    
    public Variable(String varName) {
        this.varName = varName;
    }

    String getVarName() {
        return varName;
    }

    @Override public String toString() {
        return varName;
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return ((Variable)o).getVarName().equals(varName);
    }

    @Override public int hashCode() {
        return varName.hashCode();
    }

}
