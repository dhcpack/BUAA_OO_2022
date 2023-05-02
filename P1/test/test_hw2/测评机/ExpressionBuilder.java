import java.util.Random;

public class ExpressionBuilder
{
    private static String[] numberPool =
            {
                    "0", "1", "2", "33", "111",
            };
    private static String[] exponentPool =
            {
                    "0", "2", "4", "5",
            };
    private static String[] funcNamePool =
            {
                    "f", "g", "h",
            };


    public static void Work(int numOfTerm, int numOfFactor)
    {
        System.out.println(3);
        System.out.println("h" + CustomBuilder.buildCustomFunc());
        System.out.println("f" + CustomBuilder.buildCustomFunc());
        System.out.println("g" + CustomBuilder.buildCustomFunc());
        System.out.println(buildExpression(numOfTerm, numOfFactor, "x", true));
    }

    private static String buildExpression(int numOfTerm, int numOfFactor, String name,
                                          boolean hardControl)
    {
        String s = "";
        for (int i = 0; i < numOfTerm; i++)
        {
            s += buildTerm(numOfFactor, name, hardControl) + "+";
        }

        return s.substring(0, s.length() - 1);
    }

    private static String buildTerm(int numOfFactor, String name, boolean hardControl)
    {
        String s = "";
        for (int i = 0; i < numOfFactor; i++)
        {
            s += buildFactor(name, hardControl) + "*";
        }

        return s.substring(0, s.length() - 1);
    }

    private static String buildFactor(String name, boolean hardControl)
    {
        String s = "";
        Random random = new Random();
        int type = (random.nextInt() % 6 + 6) % 6;
        switch (type)
        {
            case 1:
                s = buildExprFactor(name, hardControl);
                break;
            case 2:
                s = buildNumberFactor();
                break;
            case 3:
                s = buildTriangleFactor(name);
                break;
            case 4:
                if (hardControl)
                {
                    s = buildCustomFuncFactor(name);
                }
                else
                {
                    s = buildPowerFuncFactor(name);
                }
                break;
            case 5:
                if (hardControl)
                {
                    s = buildSumFactor(name, hardControl);
                }
                else
                {
                    s = buildNumberFactor();
                }
                break;
            default:
                s = buildPowerFuncFactor(name);
        }
        return s;
    }

    private static String buildExprFactor(String name, boolean hardControl)
    {
        String s = "(";
        s += buildExpression(2, 2, name, hardControl);
        return s + ")" + buildPower();
    }

    private static String buildNumberFactor()
    {
        String s = "";
        s += buildNP(true);
        s += getPoolFactor(numberPool);
        return s;
    }

    private static String buildTriangleFactor(String name)
    {
        String s = "";
        Random random = new Random();
        int type = (random.nextInt() % 2 + 2) % 2;

        if (type == 1)
        {
            s += "sin(";
        } else
        {
            s += "cos(";
        }

        int factorType = (random.nextInt() % 2 + 2) % 2;
        if (factorType == 1)
        {
            s += buildNumberFactor();
        }
        else
        {
            s += buildPowerFuncFactor(name);
        }
        s += ")";
        s += buildPower();
        return s;
    }

    private static String buildPowerFuncFactor(String name)
    {
        String s = name;
        s += buildPower();

        return s;
    }

    private static String buildNP(boolean control)
    {
        String s = "";

        Random random = new Random();

        int npChoice = (random.nextInt() % 3 + 3) % 3;

        if (npChoice == 1)
        {
            s += "+";
        }
        else if (npChoice == 2 && control)
        {
            s += "-";
        }

        return s;
    }

    private static String buildPower()
    {
        Random random = new Random();
        boolean isOne = random.nextBoolean();

        if (isOne)
        {
            return "";
        } else
        {
            String s = "**";
            s += buildNP(false);
            s += getPoolFactor(exponentPool);
            return s;
        }
    }

    private static String buildCustomFuncFactor(String name)
    {
        String s = "";
        Random random = new Random();
        s += getPoolFactor(funcNamePool);
        s += "(";
        for (int i = 0; i < 3; i++)
        {
            int factorType = (random.nextInt() % 3 + 3) % 3;
            switch (factorType)
            {
                case 0:
                    s += buildNumberFactor();
                    break;
                case 1:
                    s += buildPowerFuncFactor(name);
                    break;
                default:
                    s += buildTriangleFactor(name);
                    break;
            }
            s += ",";
        }
        s = s.substring(0, s.length() - 1);
        s += ")";
        return s;
    }

    private static String buildSumFactor(String name, boolean hardControl)
    {
        String s = "sum(i,";
        Random random = new Random();
        s += buildNumberFactor() + "," + buildNumberFactor() + ",";
        int factorType = random.nextInt() % 3;
        switch (factorType)
        {
            case 1:
                s += buildNumberFactor();
            case 2:
                s += buildExprFactor("i", false);
            default:
                s += buildPowerFuncFactor("i");
        }
        s += ")";
        return s;
    }

    private static String getPoolFactor(String[] pool)
    {
        Random random = new Random();

        return pool[(random.nextInt() % pool.length + pool.length) % pool.length];
    }
}
