package parse;

import ast.Node;
import ast.Variable;
import ast.Add;
import ast.Cos;
import ast.Multi;
import ast.Num;
import ast.Power;
import ast.Sin;

import java.util.HashMap;
import java.util.regex.Pattern;

public class CustomParser
{
    private Lexer lexer;
    private HashMap<String, Integer> argPosTable = new HashMap<>();
    private String name;
    private Node expr;

    public CustomParser(String input)
    {
        lexer = new Lexer(input);
    }

    public void parseCustomFunc()
    {
        name = lexer.peek();
        // 此时指向函数名
        lexer.next();
        // 此时指向 (
        int pos = 1;

        while (lexer.peek().equals(")") == false)
        {
            lexer.next();
            String arg = lexer.peek();
            argPosTable.put(arg, pos);
            pos++;

            lexer.next();
        }

        // 此时指向 (
        lexer.next();
        // 此时指向 =
        lexer.next();
        // 此时指向函数表达式的第一个元素
        expr = parseExpression();
    }

    private Node parseExpression()
    {
        Add expression = new Add();
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

    private Node parseTerm(int exprOption)
    {
        Multi term = new Multi();
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
        // 这里将正负号视为 +1 或者 -1，添加一个不存在的元素
        term.addFactor(new Num(String.valueOf(termOption)));
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
     * 所以每一个私有的子函数都要保持这个特性
     * @return
     */
    private Node parseFactor()
    {
        Node factor = null;
        // 这是表达式因子
        if (lexer.peek().equals("("))
        {
            factor = parseExprFactor();
        }
        // 这是常数因子
        else if (isInteger(lexer.peek()))
        {
            factor = new Num(getNum());
        }
        // 这是幂函数
        else if (lexer.peek().equals("x") ||
                lexer.peek().equals("y") ||
                lexer.peek().equals("z"))
        {
            factor = parseFuncPowerFactor(lexer.peek());
        }
        // 这里是三角函数
        else if (lexer.peek().equals("sin") ||
                lexer.peek().equals("cos"))
        {
            factor = parseTriangle(lexer.peek());
        }
        return factor;
    }

    private Node parseExprFactor()
    {
        // 此时指向 (
        lexer.next();
        Node expression = parseExpression();
        lexer.next();
        // 此时指向 ( 的后一个元素
        return parsePower(expression);
    }

    private Node parseFuncPowerFactor(String argName)
    {
        // 此时指向 variable
        int pos = argPosTable.get(argName);
        Node variable = new Variable(pos);
        lexer.next();
        // 此时指向 x 的后一个元素
        return parsePower(variable);
    }

    private Node parseTriangle(String name)
    {
        Node triangle = null;
        // 此时指向的是 sin
        lexer.next();
        lexer.next();
        // 此时指向的是 sin,cos 的内容
        Node factor = parseFactor();
        // 此时指向的是 )
        lexer.next();
        if (name.equals("sin"))
        {
            triangle = new Sin(factor);
        }
        else
        {
            triangle = new Cos(factor);
        }

        return parsePower(triangle);
    }

    private Node parsePower(Node coe)
    {
        String num = null;
        if (lexer.peek().equals("**"))
        {
            lexer.next();
            num = getNum();
        }
        else
        {
            num = "1";
        }
        return new Power(coe, num);
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

    public String getName()
    {
        return name;
    }

    public Node getExpr()
    {
        return expr;
    }
}
