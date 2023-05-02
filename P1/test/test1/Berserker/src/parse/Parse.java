package parse;

import expression.Expression;
import expression.Factor;
import expression.PowerFunc;
import expression.Term;
import expression.Number;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class Parse
{
    private Lexer lexer;

    public Parse(Lexer lexer)
    {
        this.lexer = lexer;
    }

    public Expression parseExpression()
    {
        Expression expression = new Expression();
        int exprOption = 1; // 默认值为+
        // 此处读取的是连接 item 的符号，如果有符号，那么需要让其再次移动一个格
        if (lexer.peek().equals("-"))
        {
            exprOption = -1;
            lexer.next();
        }
        else if (lexer.peek().equals("+"))
        {
            lexer.next();
        }
        expression.addTerm(parseTerm(exprOption));

        while (lexer.peek().equals("+") || lexer.peek().equals("-"))
        {
            exprOption = 1; // 默认值为+
            if (lexer.peek().equals("-"))
            {
                exprOption = -1;
                lexer.next();
            }
            else if (lexer.peek().equals("+"))
            {
                lexer.next();
            }
            expression.addTerm(parseTerm(exprOption));
        }

        return expression;
    }

    public Term parseTerm(int exprOption)
    {
        int termOption = exprOption;
        if (lexer.peek().equals("-"))
        {
            termOption = -termOption;
            lexer.next();
        }
        else if (lexer.peek().equals("+"))
        {
            lexer.next();
        }
        Term term = new Term(termOption);
        term.addFactor(parseFactor());

        while (lexer.peek().equals("*"))
        {
            lexer.next();
            term.addFactor(parseFactor());
        }

        return term;
    }

    /**
     * 当调用了 parseFactor 的时候，会使 peek 指向解析完 expression.Factor 后的一个语义元素
     * @return
     */
    private Factor parseFactor()
    {
        Factor factor = null;
        // 这是表达式因子
        if (lexer.peek().equals("("))
        {
            lexer.next();
            Expression expression = parseExpression();
            lexer.next();
            if (lexer.peek().equals("**"))
            {
                lexer.next();
                expression.setPower(new BigInteger(getNum()));
            }
            factor = expression;
        }
        // 这是常数因子
        else if (isInteger(lexer.peek()))
        {
            factor = new Number(getNum());
        }
        // 这是幂函数
        else if (lexer.peek().equals("x"))
        {
            lexer.next();
            // 说明是正常形式
            if (lexer.peek().equals("**"))
            {
                lexer.next();
                factor = new PowerFunc(getNum());
            }
            // 说明是缩略形式
            else
            {
                factor = new PowerFunc("1");
            }
        }
        return factor;
    }

    /**
     * 用来判断字符串是否是一个有符号的整数
     * @param str 待判断字符串
     * @return 判断结果
     */
    private boolean isInteger(String str)
    {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 读取一个有符号或者无符号的字符串，最后curToken 指向数字后的一个元素
     * @return
     */
    private String getNum()
    {
        String constant = "";
        if (lexer.peek().equals("+") || lexer.peek().equals("-"))
        {
            constant += lexer.peek();
            lexer.next();
        }
        constant += lexer.peek();
        lexer.next();
        return constant;
    }
}
