import com.oocourse.TimableOutput;

public class Print {
    private static final Print INSTANCE = new Print();

    private Print() {
    }

    public static Print getInstance() {
        return INSTANCE;
    }

    public synchronized void print(String output) {
        TimableOutput.println(output);
    }

}
