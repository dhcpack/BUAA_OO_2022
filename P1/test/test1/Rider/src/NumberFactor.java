import java.math.BigInteger;

public class NumberFactor implements Factor {
    private BigInteger number;
    private boolean sysmbol;

    public void sameAs(NumberFactor numberFactor) {
        number = numberFactor.getNumber();
        sysmbol = numberFactor.isSysmbol();
    }

    public boolean isSysmbol() {
        return sysmbol;
    }

    public void setSysmbol(boolean sysmbol) {
        this.sysmbol = sysmbol;
    }

    public NumberFactor(Lexer lexer) {
        if (lexer.peek().equals("-")) {
            sysmbol = false;
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            sysmbol = true;
            lexer.next();
        } else {
            sysmbol = true;
        }
        number = new BigInteger(lexer.peek());
        lexer.next();
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
    }

    @Override
    public String turnString() {
        if (sysmbol) {
            return number.toString();
        } else {
            return "-" + number.toString();
        }
    }

    @Override
    public void simplify() {

    }

    public void addNumber(BigInteger number2, boolean sysmbol2) {
        if (sysmbol ^ sysmbol2) {
            if (number.compareTo(number2) == 1) {
                number = number.subtract(number2);
            } else if (number.compareTo(number2) == -1) {
                number = number2.subtract(number);
                sysmbol = sysmbol2;
            } else {
                number = BigInteger.ZERO;
            }
        } else {
            number = number.add(number2);
        }
    }

    public void multNumber(BigInteger number2, boolean sysmbol2) {
        number = number.multiply(number2);
        if (!sysmbol2) {
            sysmbol = !sysmbol;
        }
    }
}
