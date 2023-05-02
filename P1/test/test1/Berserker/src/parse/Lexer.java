package parse;

public class Lexer
{
    private String input;
    private int pos = 0;
    private String curToken;

    /**
     * 利用输入的字符串构造一个 lexer
     * @param content 输入的字符串
     */
    public Lexer(String content)
    {
        input = content.replaceAll("[\\t ]", "");
        next();
    }

    /**
     * 每次调用这个方法，都会使 curToken 指向下一个元素，除非到了最后，curToken会一直指向最后一个元素
     */
    public void next()
    {
        if (pos == input.length())
        {
            return;
        }
        // 获得当前遍历到的字符串
        char c = input.charAt(pos);
        if (Character.isDigit(c))
        {
            curToken = getNumber();
        }
        else if (c == '*')
        {
            if (input.charAt(pos + 1) == '*')
            {
                pos += 2;
                curToken = "**";
            }
            else
            {
                pos++;
                curToken = "*";
            }
        }
        else if ("()+-x".indexOf(c) != -1)
        {
            pos += 1;
            curToken = String.valueOf(c);
        }
        // 对应空白字符的情况
        else
        {
            pos++;
        }
    }

    /**
     * 读取一个整数
     * @return 将整数的字符串形式返回
     */
    private String getNumber()
    {
        StringBuilder sb = new StringBuilder();
        // pos 相当于一个在原表达式 input 上移动的指针
        while (pos < input.length() && Character.isDigit(input.charAt(pos)))
        {
            sb.append(input.charAt(pos));
            pos++;
        }

        return sb.toString();
    }

    /**
     * 其实就是一个 getter
     * @return 返回的是当前的语义元素
     */
    public String peek()
    {
        return this.curToken;
    }
}
