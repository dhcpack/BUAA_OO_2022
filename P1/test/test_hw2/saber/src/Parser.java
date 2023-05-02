import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    /*
    递归解析 表达式 以+号为分隔
     */
    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+")) {
            lexer.next();
            expr.addTerm(parseTerm());
        }
        expr.setExpo(BigInteger.ONE);
        return expr;
    }

    /*
    解析项 以*号为分隔
     */
    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    /*
    解析因子 常数|幂函数|表达式
     */
    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {             //解析表达是因子
            return getExpr();
        } else if (lexer.peek().startsWith("w")) {  //解析sum求和函数
            Sum s = new Sum(lexer.peek());
            lexer.next();
            return s;
        } else if (lexer.peek().startsWith("f") | lexer.peek().startsWith("g") |
                lexer.peek().startsWith("h")) {   //解析自定义函数
            String out = CustomFunc.subs(lexer.peek());
            lexer.next();
            Lexer lexer = new Lexer(out);
            Parser parser = new Parser(lexer);
            return parser.parseExpr();
        } else if (lexer.peek().equals("u")) {  //解析sin
            return getSin();
        } else if (lexer.peek().equals("v")) {  //解析cos
            return getCos();
        } else if (lexer.peek().equals("x")) {  //解析幂函数
            return getPower();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
            return parseFactor();
        } else {                                //解析常数因子
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Const(num);
        }
    }

    /*
    获取表达式因子
     */
    public Expr getExpr() {
        lexer.next();
        Expr expr = parseExpr();
        if (lexer.peek().equals(")")) {
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                BigInteger expo = new BigInteger(lexer.peek());
                lexer.next();
                expr.setExpo(expo);
            } else {
                expr.setExpo(BigInteger.ONE);
            }
        }
        return expr;
    }

    /*
    获取幂函数因子
     */
    public Power getPower() {
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            BigInteger expo = new BigInteger(lexer.peek());
            lexer.next();
            return new Power(expo);
        } else {
            return new Power(BigInteger.ONE);
        }
    }

    /*
    获取sin因子
     */
    public Sin getSin() {
        lexer.next();
        lexer.next();
        Expr factor = parseExpr();
        BigInteger expo = BigInteger.ONE;
        if (lexer.peek().equals(")")) {
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expo = new BigInteger(lexer.peek());
                lexer.next();
            }
        }
        return new Sin(factor, expo);
    }

    /*
    获取cos因子
     */
    public Cos getCos() {
        lexer.next();
        lexer.next();
        Expr factor = parseExpr();
        BigInteger expo = BigInteger.ONE;
        if (lexer.peek().equals(")")) {
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                expo = new BigInteger(lexer.peek());
                lexer.next();
            }
        }
        return new Cos(factor, expo);
    }
}
