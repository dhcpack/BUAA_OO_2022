package taskone;

import com.oocourse.TimableOutput;

public class Output {
    public synchronized void println(Object msg) {
        TimableOutput.println(msg);
    }
}
