package tasktwo;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import taskone.Output;
import taskone.RequestQueue;
import taskone.RequestReader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task2 {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        Output output = new Output();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        ArrayList<RequestQueue> buildings = new ArrayList<>();
        ArrayList<WidthQueue> floors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RequestQueue requests = new RequestQueue();
            buildings.add(requests);
        }
        for (int i = 0; i < 10; i++) {
            WidthQueue requests = new WidthQueue();
            floors.add(requests);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Controller controller = new Controller(exec, floors, buildings, output);
        RequestReader reader = new RequestReader(elevatorInput,  controller);
        exec.execute(reader);
    }

    public Task2(String [] args) throws Exception {
        main(args);
    }
}