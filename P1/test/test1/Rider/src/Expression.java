import java.util.ArrayList;

public class Expression {
    private ArrayList<Term> terms = new ArrayList<>();

    /**
     * 新建一个表达式
     * @param lexer
     */
    public Expression(Lexer lexer) {
        Term term = new Term(lexer);
        addTerm(term);
        while (lexer.peek().equals("+") | lexer.peek().equals("-")) {
            term = new Term(lexer);
            addTerm(term);
        }
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    /**
     * 表达式增加一项
     * @param term
     */
    public void addTerm(Term term) {
        terms.add(term);
    }

    /**
     * 表达式输出
     * @return 需要输出的表达式
     */
    public String turnString() {
        String expressionString = new String();
        for (Term item : terms) {
            expressionString = expressionString + item.turnString();
        }
        if (expressionString.length() == 0) {
            //System.out.println("The expression is empty.");
            return "0";
        } else if (String.valueOf(expressionString.charAt(0)).equals("+")) {
            return expressionString.substring(1, expressionString.length());
        } else {
            return expressionString;
        }
    }

    public void simplify() {
        ArrayList<Term> newTerms = new ArrayList<>();
        for (Term item : terms) {
            item.simplify(newTerms);
        }
        terms = newTerms;
        combine();
    }

    public void combine() {
        ArrayList<Term> newTerms = new ArrayList<>();
        for (Term item : terms) {
            boolean exist = true;
            for (Term item2 : newTerms) {
                if (item2.tryCombine(item)) {
                    exist = false;
                    break;
                }
            }
            if (exist) {
                newTerms.add(item);
            }
        }
        terms = newTerms;
    }

    public Expression multExpression(Expression expression) {
        Lexer lexer = new Lexer("0");
        Expression answer = new Expression(lexer);
        for (Term item1 : terms) {
            for (Term item2 : expression.getTerms()) {
                answer.addTerm(item1.multTerm(item2));
            }
        }
        answer.combine();
        return answer;
    }
}
