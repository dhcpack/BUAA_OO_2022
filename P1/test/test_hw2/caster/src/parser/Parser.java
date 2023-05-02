package parser;

import java.math.BigInteger;
import java.util.ArrayList;

import sentence.Expre;
import sentence.Term;
import sentence.TriFunct;
import sentence.Variable;
import sentence.ConstFactor;
import sentence.CustomFunct;
import sentence.CustomFunctDefs;
import sentence.Factor;
import sentence.PowerFunct;
import sentence.ExprFactor;

public class Parser {
    
    private Lexer lexer;

    private CustomFunctDefs functs;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        functs = null;
    }

    public Parser(Lexer lexer, CustomFunctDefs functs) {
        this.lexer = lexer;
        this.functs = functs;
    }

    public Expre parseExpre() {
        Expre expression = new Expre();

        expression = expression.add(parseTerm());

        while (!lexer.reachEnd() &&
                        lexer.peek().getType() != Lexer.Type.RPAREN &&
                        (lexer.peek().getType() == Lexer.Type.PLUS ||
                                lexer.peek().getType() == Lexer.Type.MINUS)
        ) {
            expression = expression.add(parseTerm());
        }

        return expression;
    }

    private Term parseTerm() {
        Term term = new Term();

        if (parseNegative()) {
            term = term.negate();
        }
        term = term.add(parseFactor());

        while (!lexer.reachEnd() && lexer.peek().getType() == Lexer.Type.STAR) {
            lexer.next();
            term = term.add(parseFactor());
        }
        return term;
    }

    private Factor parseFactor() {
        if (lexer.peek().getType() == Lexer.Type.LPAREN) {
            return parseExprFactor();
        } else if (lexer.peek().getType() == Lexer.Type.PLUS || 
            lexer.peek().getType() == Lexer.Type.MINUS ||
            lexer.peek().getType() == Lexer.Type.CONST) {
            return parseConstFactor();
        } else {
            return parseVarFactor();
        }
    }

    public Factor parseVarFactor() {
        if (lexer.peek().getType() == Lexer.Type.VAR || 
            lexer.peek().getType() == Lexer.Type.CIRCI) {
            return parsePowerFunct();
        } else if (lexer.peek().getType() == Lexer.Type.SIN ||
            lexer.peek().getType() == Lexer.Type.COS) {
            return parseTriFunct();
        } else if (lexer.peek().getType() == Lexer.Type.SUM) {
            return parseSumFunct();
        } else {
            return parseCustomFunctCall();
        }
    }

    public ConstFactor parseConstFactor() {
        return new ConstFactor(parseSignedInt());
    }

    public ExprFactor parseExprFactor() {
        if (lexer.peek().getType() != Lexer.Type.LPAREN) {
            System.err.println("Wrong Format at " + lexer.peek().getStr());
        }
        lexer.next();

        final Expre expression = parseExpre();

        if (lexer.peek().getType() != Lexer.Type.RPAREN) {
            System.err.println("Wrong Format at " + lexer.peek().getStr());
        }
        lexer.next();

        return new ExprFactor(expression, parseIndex());
    }

    private boolean parseNegative() {
        boolean ans = false;
        if (lexer.peek().getType() == Lexer.Type.MINUS) {
            ans = true;
            lexer.next();
        } else if (lexer.peek().getType() == Lexer.Type.PLUS) {
            ans = false;
            lexer.next();
        }
        return ans;
    }

    private BigInteger parseSignedInt() {
        boolean negative = parseNegative();
        BigInteger ans = new BigInteger(lexer.peek().getStr());
        lexer.next();
        if (negative) {
            ans = ans.negate();
        }
        return ans;
    }
    
    private PowerFunct parsePowerFunct() {
        String var = lexer.peek().getStr();
        lexer.next();
        return new PowerFunct(var, parseIndex());
    }

    private TriFunct parseTriFunct() {
        final Lexer.Type type = lexer.peek().getType();
        lexer.next();   // consume SIN or COS
        lexer.next();   // consume LPAREN
        Factor inside = parseFactor();
        lexer.next();   // consume RPAREN
        return new TriFunct(type, parseIndex(), inside);
    }

    private ExprFactor parseSumFunct() {
        lexer.next();   // SUM
        lexer.next();   // LPAREN
        lexer.next();   // CIRC_I
        lexer.next();   // COMMA

        final BigInteger lowerBound = parseConstFactor().getValue();
        lexer.next();   // COMMA
        final BigInteger upperBound = parseConstFactor().getValue();
        lexer.next();   // COMMA
        Expre expre = parseExpre();
        lexer.next();   // RPAREN

        Expre expression = new Expre();
        for (BigInteger cur = lowerBound; 
                cur.compareTo(upperBound) <= 0;
                cur = cur.add(BigInteger.ONE)
        ) {
            expression = expression.add(
                new Term().add(
                    new ExprFactor(
                        expre.substitute(
                            new Variable("i"), 
                            new Expre().add(new Term().add(new ConstFactor(cur)))
                        ), 
                        new BigInteger("1")
                    )
                )
            );
        }
        return new ExprFactor(expression, new BigInteger("1"));
    }

    private ExprFactor parseCustomFunctCall() {
        final String functName = lexer.peek().getStr();
        lexer.next();   // FUNCT
        lexer.next();   // LPAREN
        ArrayList<Factor> facs = new ArrayList<>();
        facs.add(parseFactor());
        while (lexer.peek().getType() == Lexer.Type.COMMA) {
            lexer.next();   // COMMA
            facs.add(parseFactor());
        }
        lexer.next();   // RPAREN

        return functs.apply(functName, facs);
    }

    public CustomFunct parseCustomFunctDef() {
        final String functName = lexer.peek().getStr();
        lexer.next();
        lexer.next();
        ArrayList<Variable> vars = new ArrayList<>();
        vars.add(new Variable(lexer.peek().getStr()));
        lexer.next();
        while (lexer.peek().getType() == Lexer.Type.COMMA) {
            lexer.next();
            vars.add(new Variable(lexer.peek().getStr()));
            lexer.next();
        }
        lexer.next();
        lexer.next();
        Expre definition = parseExpre();
        return new CustomFunct(functName, vars, definition);
    }

    public BigInteger parseIndex() {
        if (lexer.peek().getType() == Lexer.Type.EXP) {
            lexer.next();
            return parseSignedInt();
        } else {
            return new BigInteger("1");
        }
    }

}
