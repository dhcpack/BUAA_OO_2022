import com.oocourse.TimableOutput;

public class OutputQueue {
    public synchronized void print(String message) {
        TimableOutput.println(message);
    }
}
