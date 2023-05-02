import com.oocourse.TimableOutput;

public class Output extends TimableOutput {
    private Output(){}

    private static final Output INSTANCE = new Output();

    public static Output getInstance() {
        return INSTANCE;
    }

    public synchronized long print(Object obj) {
        notifyAll();
        return TimableOutput.println(obj);
    }
}
