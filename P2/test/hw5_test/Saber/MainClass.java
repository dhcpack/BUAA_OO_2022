import com.oocourse.TimableOutput;
import com.oocourse.elevator1.ElevatorInput;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass {
    private static InputHandler inputHandler;
    private static HashMap<Character, Building> buildings = new HashMap<>();

    public static void init() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        inputHandler = new InputHandler(elevatorInput);
        for (int i = 0; i < 5; ++i) {
            Building building = new Building(String.valueOf((char)(i + 65)));
            buildings.put((char)(i + 65), building);
            inputHandler.addBuilding(building);
        }
    }

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        init();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(inputHandler);
        for (int i = 0; i < 5; ++i) {
            executorService.execute(new Elevator(i + 1, buildings.get((char)(i + 65))));
        }
        executorService.shutdown();
    }
}
