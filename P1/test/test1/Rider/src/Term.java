import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();

    public Term(Lexer lexer) {
        if (lexer.peek().equals("-")) {
            Lexer space = new Lexer("-1");
            NumberFactor sysmbolFactor = new NumberFactor(space);
            factors.add(sysmbolFactor);
            lexer.next();
        } else if ((lexer.peek().equals("+"))) {
            lexer.next();
        }
        if (lexer.peek().equals("x")) {
            SimpleFactor simpleFactor = new SimpleFactor(lexer);
            addFactor(simpleFactor);
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            NumberFactor numberFactor = new NumberFactor(lexer);
            addFactor(numberFactor);
        } else if (String.valueOf(lexer.peek().charAt(0)).equals("(")) {
            ComplexFactor complexFactor = new ComplexFactor(lexer);
            addFactor(complexFactor);
        } else {
            System.out.println(lexer.peek() +
                    " is not a factor." + "The pos is" + lexer.getPos());
        }
        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("x")) {
                SimpleFactor simpleFactor = new SimpleFactor(lexer);
                addFactor(simpleFactor);
            } else if (Character.isDigit(lexer.peek().charAt(0)) |
                    lexer.peek().equals("-") | lexer.peek().equals("+")) {
                NumberFactor numberFactor = new NumberFactor(lexer);
                addFactor(numberFactor);
            } else if (String.valueOf(lexer.peek().charAt(0)).equals("(")) {
                ComplexFactor complexFactor = new ComplexFactor(lexer);
                addFactor(complexFactor);
            } else {
                System.out.println(lexer.peek() +
                        " is not a factor." + "The pos is" + lexer.getPos());
            }
        }
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public String turnString() {
        String str = new String("");
        NumberFactor numberFactor = this.hasNumber();
        if (numberFactor == null) {
            str = str + "+";
        } else if (numberFactor.getNumber().compareTo(BigInteger.ZERO) == 0) {
            return "";
        } else if (numberFactor.isSysmbol() == true) {
            str = str + "+";
        } else if (numberFactor.isSysmbol() == false) {
            str = str + "-";
            numberFactor.setSysmbol(true);
        }
        for (Factor item : factors) {
            if (item instanceof NumberFactor) {
                if (((NumberFactor) item).getNumber().compareTo(BigInteger.ONE) == 0
                        && factors.size() > 1) {
                    continue;
                }
            }
            str = str + item.turnString() + "*";
        }
        return str.substring(0, str.length() - 1);
    }

    public void simplify(ArrayList<Term> allTerm) {
        for (Factor item : factors) {
            item.simplify();
        }
        Lexer lexer = new Lexer("1");
        Expression answer = new Expression(lexer);
        lexer = new Lexer("1");
        Term deleteTerm = new Term(lexer);
        for (Factor item : factors) {
            if (item instanceof ComplexFactor) {
                answer = answer.multExpression(((ComplexFactor) item).getExpression());
            } else {
                deleteTerm.addFactor(item);
            }
        }
        Lexer lexer2 = new Lexer("0");
        Expression termExpression = new Expression(lexer2);
        termExpression.addTerm(deleteTerm);
        answer = answer.multExpression(termExpression);
        for (Term item : answer.getTerms()) {
            item.combine();
            allTerm.add(item);
        }
    }

    public void combine() {
        SimpleFactor simpleFactor = null;
        NumberFactor numberFactor = null;
        for (Factor item : factors) {
            if (item instanceof SimpleFactor) {
                if (simpleFactor == null) {
                    Lexer lexer = new Lexer("x");
                    simpleFactor = new SimpleFactor(lexer);
                    simpleFactor.sameAs((SimpleFactor) item);
                } else {
                    simpleFactor.setPow(simpleFactor.getPow() + ((SimpleFactor) item).getPow());
                }
            } else if (item instanceof NumberFactor) {
                if (numberFactor == null) {
                    Lexer lexer = new Lexer("0");
                    numberFactor = new NumberFactor(lexer);
                    numberFactor.sameAs((NumberFactor) item);
                } else {
                    numberFactor.multNumber(((NumberFactor) item).getNumber()
                            , ((NumberFactor) item).isSysmbol());
                }
            } else {
                System.out.println("It's not a right factor");
            }
        }
        factors = new ArrayList<>();
        if (simpleFactor != null) {
            factors.add(simpleFactor);
        }
        if (numberFactor != null) {
            factors.add(numberFactor);
        }
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public Term multTerm(Term term) {
        Lexer lexer = new Lexer("1");
        Term answer = new Term(lexer);
        for (Factor item : factors) {
            answer.addFactor(item);
        }
        for (Factor item : term.getFactors()) {
            answer.addFactor(item);
        }
        answer.combine();
        return answer;
    }

    public boolean tryCombine(Term term) {
        if (factors.size() == 1 && term.getFactors().size() == 1) {
            if (factors.get(0) instanceof NumberFactor &&
                    term.getFactors().get(0) instanceof NumberFactor) {
                BigInteger number2 = ((NumberFactor) term.getFactors().get(0)).getNumber();
                boolean sysmbol2 = ((NumberFactor) term.getFactors().get(0)).isSysmbol();
                ((NumberFactor) factors.get(0)).addNumber(number2, sysmbol2);
                return true;
            }
        }
        for (Factor item1 : factors) {
            for (Factor item2 : term.getFactors()) {
                if (item1 instanceof SimpleFactor && item2 instanceof SimpleFactor) {
                    if (((SimpleFactor) item1).getPow() == ((SimpleFactor) item2).getPow()) {
                        NumberFactor numberFactor1 = this.hasNumber();
                        NumberFactor numberFactor2 = term.hasNumber();
                        if (numberFactor1 != null && numberFactor2 != null) {
                            numberFactor1.addNumber(numberFactor2.getNumber()
                                    , numberFactor2.isSysmbol());
                        } else if (numberFactor1 != null && numberFactor2 == null) {
                            numberFactor1.addNumber(BigInteger.valueOf(1), true);
                        } else if (numberFactor2 != null) {
                            numberFactor2.addNumber(BigInteger.valueOf(1), true);
                            this.addFactor(numberFactor2);
                        } else {
                            Lexer lexer = new Lexer("0");
                            NumberFactor numberFactor = new NumberFactor(lexer);
                            numberFactor.setNumber(BigInteger.valueOf(2));
                            numberFactor.setSysmbol(true);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public NumberFactor hasNumber() {
        for (Factor item : factors) {
            if (item instanceof NumberFactor) {
                return (NumberFactor) item;
            }
        }
        return null;
    }

    public ComplexFactor hasComplexFactor() {
        for (Factor item : factors) {
            if (item instanceof ComplexFactor) {
                return (ComplexFactor) item;
            }
        }
        return null;
    }
}
