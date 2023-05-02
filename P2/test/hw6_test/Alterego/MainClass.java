import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestPool requestPool = new RequestPool();
        InputHandler inputHandler = new InputHandler(requestPool);
        Elevator[] elevators = new Elevator[5];
        for (int i = 0; i < 5; i++) {
            elevators[i] = new Elevator(requestPool, (char)('A' + i), i + 1);
        }
        for (int i = 0; i < 5; i++) {
            elevators[i].start();
        }
        inputHandler.start();
    }
}
