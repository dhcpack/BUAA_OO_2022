import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Input input = Input.getInstance();
        input.start();
        ElevatorSystem e = ElevatorSystem.getInstance();
        e.start();
    }
}
