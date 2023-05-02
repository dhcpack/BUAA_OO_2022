import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ReqHandler reqHandler = new ReqHandler();
        for (char b = 'A'; b <= 'E'; b++) {
            ReqPool pool = new ReqPool();
            Elevator elevator = new Elevator(b, pool);
            elevator.start();
            reqHandler.init(b, pool);
        }
        reqHandler.start();
    }
}
