import com.oocourse.TimableOutput;

public class OutputWrapper {
    public static synchronized void println(String string) {
        TimableOutput.println(string);
    }
}
