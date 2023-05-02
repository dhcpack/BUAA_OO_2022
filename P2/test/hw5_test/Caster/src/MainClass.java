import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        MyQueue<PersonRequest> inputQueue = new MyQueue<>();

        NewMainBuilding newMainBuilding = new NewMainBuilding(inputQueue);
        newMainBuilding.start();

        InputThread inputThread = new InputThread(inputQueue);
        inputThread.start();
    }
}
