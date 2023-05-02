package parse;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor
{
    /**
     * 首先是以函数名为 key，查找具体的函数，然后是是以参数的位置为名，查找形参
     */
    private HashMap<String, HashMap<Integer, String>> functions = new HashMap<>();

    /**
     * 这里的定义的属性，主要是为了解决不同个数参数的匹配问题
     */
    private final String funName = "(?<name>[fgh])";
    private final String content = "(?<content>.+)";
    private final String regex3 = funName +
            "\\((?<arg1>[xyz]),(?<arg2>[xyz]),(?<arg3>[xyz])\\)" +
            "=" +
            content;
    private final String regex2 = funName +
            "\\((?<arg1>[xyz]),(?<arg2>[xyz])\\)" +
            "=" +
            content;
    private final String regex1 = funName +
            "\\((?<arg1>[xyz])\\)" +
            "=" +
            content;
    private final String argument = "(\\d+)|" +
            "(x[\\*\\d\\+-]*)|" +
            "sin\\((\\d+)|(x[\\*\\d\\+-]*)\\)[\\*\\d\\+-]*" +
            "cos\\((\\d+)|(x[\\*\\d\\+-]*)\\)[\\*\\d\\+-]*";
    private final String regex4 = funName +
            "\\((?<arg1>" +
            argument +
            ")\\)";
    private final String regex5 = funName +
            "\\((?<arg1>" +
            argument +
            "),(?<arg2>" +
            argument +
            ")\\)";
    private final String regex6 = funName +
            "\\((?<arg1>" +
            argument +
            "),(?<arg2>" +
            argument +
            "),(?<arg3>" +
            argument +
            ")\\)";

    private final Pattern pattern1 = Pattern.compile(regex1);
    private final Pattern pattern2 = Pattern.compile(regex2);
    private final Pattern pattern3 = Pattern.compile(regex3);
    private final Pattern pattern4 = Pattern.compile(regex4);
    private final Pattern pattern5 = Pattern.compile(regex5);
    private final Pattern pattern6 = Pattern.compile(regex6);

    private final String sumFunc = "sum\\(i,(<?begin>\\d+),(<?end>\\d+),(<?expr>.+)\\)";
    private final Pattern pattern7 = Pattern.compile(sumFunc);

    private String slimSpace(String s)
    {
        return s.replaceAll("[\\t ]", "");
    }

    /**
     * 用于将自定义函数转换成方便查询的格式
     * @param input 一条自定义函数字符串
     */
    public void addFunction(String input)
    {
        String s = slimSpace(input);
        Matcher matcher1 = pattern1.matcher(s);
        Matcher matcher2 = pattern2.matcher(s);
        Matcher matcher3 = pattern3.matcher(s);
        HashMap<Integer, String> tmp = new HashMap<>();
        if (matcher1.find())
        {
            tmp.put(0, matcher1.group("content"));
            tmp.put(1, matcher1.group("arg1"));
            functions.put(matcher1.group("name"), tmp);
        }
        else if (matcher2.find())
        {
            tmp.put(0,matcher2.group("content"));
            tmp.put(1, matcher2.group("arg1"));
            tmp.put(2, matcher2.group("arg2"));
            functions.put(matcher2.group("name"), tmp);
        }
        else if (matcher3.find())
        {
            tmp.put(0, matcher3.group("content"));
            tmp.put(1, matcher3.group("arg1"));
            tmp.put(2, matcher3.group("arg2"));
            tmp.put(3, matcher3.group("arg3"));
            functions.put(matcher3.group("name"), tmp);
        }
        else
        {
            System.out.println("Wrong function!");
        }
    }

    /**
     * 将里面含有自定义函数的表达式中的自定义函数代入成没有自定义函数的形式
     * @param input 未处理的表达式
     * @return 没有自定义函数的表达式字符串
     */
    private String substitute(String input)
    {
        String s = input;
        Matcher matcher4 = pattern4.matcher(s);
        Matcher matcher5 = pattern5.matcher(s);
        Matcher matcher6 = pattern6.matcher(s);

        while (matcher4.find())
        {
            HashMap<Integer, String> tmpFunc = functions.get(matcher4.group("name"));
            String tmpContent = tmpFunc.get(0);
            String tmpArg1 = tmpFunc.get(1);
            String tmpSub = tmpContent.replaceAll(tmpArg1,
                    "(" + matcher4.group("arg1") + ")");
            System.out.println(matcher4.group(0));
            s = s.replaceAll(matcher4.group(0), tmpSub);
            System.out.println(s);
        }

        while (matcher5.find())
        {
            HashMap<Integer, String> tmpFunc = functions.get(matcher5.group("name"));
            String tmpContent = tmpFunc.get(0);
            String tmpArg1 = tmpFunc.get(1);
            String tmpArg2 = tmpFunc.get(2);
            String tmpSub = tmpContent.replaceAll(tmpArg1,
                    "(" + matcher5.group("arg1") + ")");
            tmpSub = tmpSub.replaceAll(tmpArg2,
                    "(" + matcher5.group("arg2") + ")");
            s = s.replaceAll(matcher5.group(0), tmpSub);
        }

        while (matcher6.find())
        {
            HashMap<Integer, String> tmpFunc = functions.get(matcher6.group("name"));
            String tmpContent = tmpFunc.get(0);
            String tmpArg1 = tmpFunc.get(1);
            String tmpArg2 = tmpFunc.get(2);
            String tmpArg3 = tmpFunc.get(0);
            String tmpSub = tmpContent.replaceAll(tmpArg1,
                    "(" + matcher6.group("arg1") + ")");
            tmpSub = tmpSub.replaceAll(tmpArg2,
                    "(" + matcher6.group("arg2") + ")");
            tmpSub = tmpSub.replaceAll(tmpArg3,
                    "(" + matcher6.group("arg3") + ")");
            s = s.replaceAll(matcher6.group(0), tmpSub);
        }

        return s;
    }

    /**
     * 用于处理 sum 函数
     * @param input 一个已经经过自定义函数代入的表达式字符串
     * @return 一个展开 sum 函数的字符串
     */
    private String handleSum(String input)
    {
        String ans = input;
        Matcher matcher = pattern7.matcher(input);

        while (matcher.find())
        {
            String expr = matcher.group("expr");
            long begin = Long.parseLong(matcher.group("begin"));
            long end = Long.parseLong(matcher.group("end"));
            // 如果是 begin > end 那么就是不存在这个 sum 了
            if (begin - end > 0)
            {
                ans = ans.replaceAll(matcher.group(0), "0");
            }
            else
            {
                // 此时说明求和表达式里没有 i
                if (expr.indexOf('i') == -1)
                {
                    ans = ans.replaceAll(matcher.group(0), (end - begin + 1) + "*" +
                            matcher.group("expr"));
                }
                // 表达式里面有 i
                else
                {
                    StringBuilder tmp = new StringBuilder("+");
                    for (long i = begin; i <= end; i++)
                    {
                        tmp.append(matcher.group("expr").replaceAll("i", "" + i));
                    }

                    ans = ans.replaceAll(matcher.group(0), tmp.toString());
                }
            }
        }
        return ans;
    }

    public String preProcess(String input)
    {
        String ans = slimSpace(input);
        ans = substitute(ans);
        ans = handleSum(ans);
        return ans;
    }
}
