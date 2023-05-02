package simplifiedexpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SimplifiedTerm {
    private final HashMap<SimplifiedBase, Integer> simplifiedBases;

    public SimplifiedTerm() {
        simplifiedBases = new HashMap<>();
    }

    public SimplifiedTerm(SimplifiedTerm simplifiedTerm) {
        simplifiedBases = new HashMap<>();
        for (SimplifiedBase simplifiedBase : simplifiedTerm.simplifiedBases.keySet()) {
            if (simplifiedBase instanceof SimplifiedVar) {
                simplifiedBases.put(new SimplifiedVar((SimplifiedVar) simplifiedBase),
                        new Integer(simplifiedTerm.simplifiedBases.get(simplifiedBase)));
            } else if (simplifiedBase instanceof SimplifiedFunc) {
                simplifiedBases.put(new SimplifiedFunc((SimplifiedFunc) simplifiedBase),
                        new Integer(simplifiedTerm.simplifiedBases.get(simplifiedBase)));
            }
        }
    }

    public HashMap<SimplifiedBase, Integer> getSimplifiedBases() {
        return simplifiedBases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedTerm that = (SimplifiedTerm) o;
        return Objects.equals(simplifiedBases, that.simplifiedBases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simplifiedBases);
    }

    public SimplifiedTerm multiply(SimplifiedTerm multiplicand) {
        SimplifiedTerm result = new SimplifiedTerm();
        for (SimplifiedBase simplifiedBase : this.simplifiedBases.keySet()) {
            result.simplifiedBases.put(simplifiedBase,
                    this.simplifiedBases.get(simplifiedBase));
        }
        for (SimplifiedBase simplifiedBase : multiplicand.simplifiedBases.keySet()) {
            if (result.simplifiedBases.containsKey(simplifiedBase)) {
                result.simplifiedBases.put(simplifiedBase, result.simplifiedBases.
                        get(simplifiedBase) + multiplicand.simplifiedBases.get(simplifiedBase));
            } else {
                result.simplifiedBases.put(simplifiedBase,
                        multiplicand.simplifiedBases.get(simplifiedBase));
            }
        }
        return result;
    }

    public ArrayList<SimplifiedBase> func() {
        ArrayList<SimplifiedBase> ret = new ArrayList<>();
        for (SimplifiedBase simplifiedBase : simplifiedBases.keySet()) {
            if (simplifiedBase instanceof SimplifiedFunc) {
                SimplifiedFunc simplifiedFunc = (SimplifiedFunc) simplifiedBase;
                if (simplifiedFunc.getFuncName().equals("sin")
                        && simplifiedBases.get(simplifiedBase) >= 2) {
                    ret.add(new SimplifiedFunc(simplifiedFunc));
                }
            }
        }
        return ret;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SimplifiedBase simplifiedBase : simplifiedBases.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("*");
            }
            stringBuilder.append(simplifiedBase);
            if (simplifiedBase instanceof SimplifiedVar &&
                    simplifiedBases.get(simplifiedBase).equals(2)) {
                stringBuilder.append("*x");
            } else if (!simplifiedBases.get(simplifiedBase).equals(1)) {
                stringBuilder.append("**");
                stringBuilder.append(simplifiedBases.get(simplifiedBase));
            }
        }
        return String.valueOf(stringBuilder);
    }
}
