package parser;

import expr.Expr;
import expr.Var;
import expr.Term;
import expr.Number;
import expr.Factor;
import lex.Lexer;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        String coe = "";
        parseBracket();
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            coe = lexer.peek();
            lexer.next();
            parseBracket();
        }
        expr.addTerm(parseTerm(coe));
        parseBracket();

        coe = "";
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            coe = lexer.peek();
            lexer.next();
            parseBracket();
            expr.addTerm(parseTerm(coe));
            parseBracket();
        }
        return expr;
    }

    public Term parseTerm(String coe) {
        int newCoe;
        if (coe.equals("-")) {
            newCoe = -1;
        } else {
            newCoe = 1;
        }
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (!coe.equals(lexer.peek())) {
                newCoe = -1;
            } else {
                newCoe = 1;
            }
            lexer.next();
            parseBracket();
        }
        Term term = new Term();
        term.addFactor(new Number(BigInteger.valueOf(newCoe)));
        term.addFactor(parseFactor());

        parseBracket();
        while (lexer.peek().equals("*")) {
            lexer.next();
            parseBracket();
            term.addFactor(parseFactor());
            parseBracket();
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            return parseExprFactor();
        } else if (lexer.peek().matches("[a-zA-Z]")) {
            return parseVarFactor();
        } else {
            return parseNumberFactor();
        }
    }

    public Var parseVarFactor() {
        Var var = new Var();
        var.setName(lexer.peek());
        lexer.next();
        parseBracket();
        if (lexer.peek().equals("^")) {
            lexer.next();
            parseBracket();
            if (lexer.peek().matches("[+-]") || lexer.peek().matches("[0-9]+")) {
                var.setIndex(Integer.parseInt(parseNum().toString()));
                lexer.next();
            }
        }
        return var;
    }

    public Expr parseExprFactor() {
        lexer.next();
        Expr expr = parseExpr();
        if (lexer.peek().equals(")")) {
            lexer.next();
            parseBracket();
            if (lexer.peek().equals("^")) {
                lexer.next();
                parseBracket();
                if (lexer.peek().matches("[+-]") || lexer.peek().matches("[0-9]+")) {
                    expr.setIndex(Integer.parseInt(parseNum().toString()));
                    lexer.next();
                }
            }
        } else {
            System.out.println("Wrong format");
        }
        return expr;
    }

    public Number parseNumberFactor() {
        Number number = new Number(parseNum());
        lexer.next();
        return number;
    }

    public BigInteger parseNum() {
        BigInteger num = new BigInteger("1");
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            lexer.next();
            num = num.negate();
        }
        num = num.multiply(new BigInteger(lexer.peek()));
        return num;
    }

    public void parseBracket() {
        while (lexer.peek().equals(" ") || lexer.peek().equals("\t")) {
            lexer.next();
        }
    }

}

