import java.util.ArrayList;

public class Kop {
    private String input;
    private ArrayList<String> kops = new ArrayList<>();

    public void setInput(String input) {
        this.input = input;
    }

    public ArrayList<String> getKops() {
        return this.kops;
    }

    public void setTerms() {
        StringBuilder stringBuilder = new StringBuilder(input);
        int k = 0;
        for (int j = 0; j < stringBuilder.length() - 1;j++) {
            if (stringBuilder.charAt(j) == '(') {
                k = 1;
            }
            if (stringBuilder.charAt(j) == ')') {
                k = 0;
            }
            boolean a = (stringBuilder.charAt(j) == 'x') && (stringBuilder.charAt(j + 1) == '+'
                    || stringBuilder.charAt(j + 1) == '-');
            boolean b = (Character.isDigit(stringBuilder.charAt(j))) && (stringBuilder.charAt(j + 1)
                    == '+' || stringBuilder.charAt(j + 1) == '-');
            boolean c = (stringBuilder.charAt(j) == ')') && (stringBuilder.charAt(j + 1) == '+'
                    || stringBuilder.charAt(j + 1) == '-');
            if (k == 0 && (a || b || c)) {
                stringBuilder.insert(j + 1,"#");
            }
        }
        String s = stringBuilder.toString();
        String[] q = s.split("#");
        for (int i = 0; i < q.length; i++) {
            kops.add(q[i]);
        }
    }

}
