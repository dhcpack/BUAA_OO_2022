import java.util.Random;

public class CustomBuilder
{
    private static String[] numberPool =
            {
                    "0", "1", "2", "33", "111",
            };
    private static String[] exponentPool =
            {
                    "0", "2", "4", "5",
            };

    private static String[] arg3 =
            {
                    "x, y, z", "x, z, y",
                    "y, x, z", "y, z, x",
                    "z, x, y", "z, y, z"
            };
    private static String[] argName =
            {
                    "x", "y", "z"
            };


    public static String buildCustomFunc()
    {
        String s = "(";
        s += getPoolFactor(arg3);
        s += ")=";
        s += buildExpression(3, 3);
        return s;
    }

    private static String buildExpression(int numOfTerm, int numOfFactor)
    {
        String s = "";
        for (int i = 0; i < numOfTerm; i++)
        {
            s += buildTerm(numOfFactor) + "+";
        }

        return s.substring(0, s.length() - 1);
    }

    private static String buildTerm(int numOfFactor)
    {
        String s = "";
        for (int i = 0; i < numOfFactor; i++)
        {
            s += buildFactor() + "*";
        }

        return s.substring(0, s.length() - 1);
    }

    private static String buildFactor()
    {
        String s = "";
        Random random = new Random();
        int type = random.nextInt() % 3;

        switch (type)
        {
            case 1:
                s = buildExprFactor();
                break;
            case 2:
                s = buildTriangleFactor();
                break;
            default:
                s = buildNumberFactor();
        }
        return s;
    }

    private static String buildExprFactor()
    {
        String s = "(";
        s += buildExpression(2, 2);
        return s + ")" + buildPower();
    }

    private static String buildNumberFactor()
    {
        String s = "";
        s += buildNP(true);

        s += getPoolFactor(numberPool);

        return s;
    }

    private static String buildTriangleFactor()
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
        } else
        {
            s += buildPowerFuncFactor();
        }
        s += ")";
        s += buildPower();
        return s;
    }

    private static String buildPowerFuncFactor()
    {
        String s = "";
        s += getPoolFactor(argName);
        s += buildPower();
        return s;
    }

    private static String buildNP(boolean control)
    {
        String s = "";

        Random random = new Random();

        int npChoice = (random.nextInt() % 3) % 3;

        if (npChoice == 1)
        {
            s += "+";
        } else if (npChoice == 2 && control)
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
            s += getPoolFactor(numberPool);
            return s;
        }
    }

    private static String getPoolFactor(String[] pool)
    {
        Random random = new Random();

        return pool[(random.nextInt() % pool.length + pool.length) % pool.length];
    }
}
