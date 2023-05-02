package sentence;

import java.util.ArrayList;

public class CustomFunct {
   
    private String functName;

    private ArrayList<Variable> vars;

    private Expre definition;

    public CustomFunct(String functName, ArrayList<Variable> vars, Expre definition) {
        this.functName = functName;
        this.vars = vars;
        this.definition = definition;
    }

    public String getFunctName() {
        return functName;
    }

    public Expre apply(ArrayList<Factor> tars) {
        Expre ans = definition;
        for (int i = 0; i < vars.size(); ++i) {
            ArrayList<Term> terms = new ArrayList<>();
            ArrayList<Factor> factors = new ArrayList<>();
            factors.add(tars.get(i));
            terms.add(new Term(factors, true));
            Expre target = new Expre(terms);
            ans = ans.substitute(vars.get(i), target);
        }
        return ans;
    }

}
