import com.oocourse.TimableOutput;

public class TimeOutput {

    public synchronized void printOutput(String toPrint) {
        TimableOutput.println(toPrint);
    }
}
