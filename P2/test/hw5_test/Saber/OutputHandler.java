import com.oocourse.TimableOutput;

public class OutputHandler {
    public static synchronized void print(String info) {
        TimableOutput.println(info);
    }
}
