import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) throws IOException, InterruptedException {
        HashMap<Character, Elevator> elevators = new HashMap<>();
        HashMap<Character, RequestTransfer> requestTransfers = new HashMap<>();
        HashMap<Character, Thread> threads = new HashMap<>();

        for (int i = 0; i < 5; i++) {  // 开5个电梯线程
            RequestTransfer requestTransfer = new RequestTransfer();
            Elevator elevator = new Elevator(i, requestTransfer);
            requestTransfers.put((char) ('A' + i), requestTransfer);
            elevators.put((char) ('A' + i), elevator);
            Thread thread = new Thread(elevator);
            threads.put((char) ('A' + i), thread);
            thread.start();
        }

        TimableOutput.initStartTimestamp();

        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                for (RequestTransfer requestTransfer : requestTransfers.values()) {
                    requestTransfer.setEnd(true);
                }
                break;
            } else {
                requestTransfers.get(request.getFromBuilding()).inputRequest(request);
            }
        }
        elevatorInput.close();

        for (Thread thread : threads.values()) {
            thread.join();
        }
    }
}