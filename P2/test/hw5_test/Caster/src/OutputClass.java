import com.oocourse.TimableOutput;

public class OutputClass {
    public static synchronized void println(String message) {
        TimableOutput.println(message);
    }
}
