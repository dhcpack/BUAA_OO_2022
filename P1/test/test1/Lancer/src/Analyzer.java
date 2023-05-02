import java.math.BigInteger;

public class Analyzer {
    private final InputData calcstr;

    public Analyzer(InputData newcalc) {
        calcstr = newcalc;
    }

    public Result readExpr() {
        boolean needNeg = false;
        Result ret;
        if (calcstr.getNowchar() == '-') {  //first term sign
            needNeg = true;
            calcstr.moveTonext();
        } else {
            if (calcstr.getNowchar() == '+') {
                calcstr.moveTonext();
            }
        }
        if (needNeg) {
            ret = readTerm().toNeg();
        } else {
            ret = readTerm();
        }
        while (!calcstr.reachEnd() &&
                (calcstr.getNowchar() == '+' || calcstr.getNowchar() == '-')) {
            if (calcstr.getNowchar() == '+') {
                calcstr.moveTonext();
                ret = ret.plus(readTerm());
            } else if (calcstr.getNowchar() == '-') {
                calcstr.moveTonext();
                ret = ret.minus(readTerm());
            }
        }
        return ret;
    }

    public Result readTerm() {
        boolean needNeg = false;
        if (calcstr.getNowchar() == '-') {
            needNeg = true;
            calcstr.moveTonext();
        } else if (calcstr.getNowchar() == '+') {
            calcstr.moveTonext();
        }
        Result ret = readFactor();
        while (!calcstr.reachEnd() && calcstr.getNowchar() == '*') {
            calcstr.moveTonext();
            ret = ret.multiply(readFactor());
        }
        if (needNeg) {
            ret = ret.toNeg();
        }
        return ret;
    }

    public Result readFactor() {
        Result ret = new Result();
        if (calcstr.getNowchar() == '(') {
            calcstr.moveTonext();  //!
            ret = readExpr();
            calcstr.moveTonext();  //expect a ')'
            if (calcstr.checkPower()) {
                BigInteger t2 = calcstr.readAnumber();
                ret = ret.getpow(t2.longValue());
            }
        } else if (Character.isDigit(calcstr.getNowchar())
                || (calcstr.getNowchar() == '+') || (calcstr.getNowchar() == '-')) {  //signed 0-9
            BigInteger t1 = calcstr.readAnumber();
            ret.coefset(0, t1);
        } else if (calcstr.getNowchar() == 'x') {
            calcstr.moveTonext();//!
            if (calcstr.checkPower()) {
                BigInteger t2 = calcstr.readAnumber();
                String tmp = t2.toString();
                ret.coefset(Integer.parseInt(tmp), BigInteger.ONE);
            } else {
                ret.coefset(1, BigInteger.ONE);
            }
        }
        return ret;
    }

}
