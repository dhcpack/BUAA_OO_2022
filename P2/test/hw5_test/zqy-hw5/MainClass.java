import java.util.HashMap;
import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class MainClass {
    public static void main(String[] args) throws Exception {
        // BEGIN
        // - init time'd output
        TimableOutput.initStartTimestamp();
        // - start elevator threads
        HashMap<Character, ThreadElevator> elevators = new HashMap<>();
        for (int id = 0; id < 5; id++) {
            char building = (char) ('A' + id);
            ThreadElevator elev = new ThreadElevator(1 + id, building);
            elevators.put(building, elev);
            elev.start();
        }

        // BODY
        // - input loop
        PersonRequest req;
        ElevatorInput eIn = new ElevatorInput(System.in);
        while ((req = eIn.nextPersonRequest()) != null) {
            elevators.get(req.getFromBuilding()).getQueue().put(req);
        }
        eIn.close();

        // END
        // - request elevator threads to stop
        for (ThreadElevator elev : elevators.values()) {
            elev.getQueue().end();
            elev.join();
        }
    }
}
