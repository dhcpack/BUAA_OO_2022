package sentence;

import java.math.BigInteger;
import java.util.ArrayList;

public class CustomFunctDefs {
    private final ArrayList<CustomFunct> functions;

    public CustomFunctDefs() {
        functions = new ArrayList<>();
    }

    public CustomFunctDefs(ArrayList<CustomFunct> functions) {
        this.functions = new ArrayList<CustomFunct>(functions);
    }

    public CustomFunctDefs add(CustomFunct funct) {
        ArrayList<CustomFunct> newFuncts = new ArrayList<>(functions);
        newFuncts.add(funct);
        return new CustomFunctDefs(newFuncts);
    }

    public ExprFactor apply(String functName, ArrayList<Factor> facts) {
        for (CustomFunct fun : functions) {
            if (fun.getFunctName().equals(functName)) {
                return new ExprFactor(fun.apply(facts), new BigInteger("1"));
            }
        }
        return null;
    }
}
