import java.math.BigInteger;

public class InputData {
    private final String dealwith;
    private int now;

    public InputData(String newstr) {
        dealwith = newstr;
        now = 0;
    }

    public BigInteger readAnumber() {
        boolean needNeg = false;
        if (dealwith.charAt(now) == '-') {
            needNeg = true;
        }
        if (!Character.isDigit(dealwith.charAt(now))) {
            moveTonext();
        }
        int start;
        int end;
        start = now;
        end = now + 1;
        while (end < dealwith.length() && Character.isDigit(dealwith.charAt(end))) {
            end++;
        }
        String tmp;
        tmp = dealwith.substring(start, end);
        now = end;
        BigInteger ret = new BigInteger(tmp);
        if (needNeg) {
            ret = ret.negate();
        }
        return ret;

    }

    public char getNowchar() {
        return this.dealwith.charAt(now);
    }

    public boolean checkPower() {
        if (now + 1 < dealwith.length() &&
                dealwith.charAt(now) == '*' && dealwith.charAt(now + 1) == '*') {
            now = now + 2; //going to read a number
            return true;
        } else {
            return false;
        }

    }

    public void moveTonext() {
        if (now < dealwith.length()) {
            now++;
        }
    }

    public boolean reachEnd() {
        if (now >= dealwith.length() - 1) { //!
            return true;
        } else {
            return false;
        }
    }

}
