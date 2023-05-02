import java.util.ArrayList;

public class Cop {
    private String input;
    private ArrayList<String> factors = new ArrayList<>();

    public void setInput(String input) {
        this.input = input;
    }

    public void setCops() {
        StringBuilder stringBuilder = new StringBuilder(input);
        int k = 0;
        for (int j = 0; j < stringBuilder.length();j++)
        {
            if (stringBuilder.charAt(j) == '(') {
                k = 1;
            }
            if (stringBuilder.charAt(j) == ')') {
                k = 0;
            }
            if (k == 0 && stringBuilder.charAt(j) == '*') {
                stringBuilder.setCharAt(j,'&');
            }
        }
        String s = stringBuilder.toString();
        String[] b = s.split("&");
        for (int i = 0;i < b.length;i++) {
            factors.add(b[i]);
        }
    }

    public ArrayList<String> getFactors() {
        return this.factors;
    }
}
