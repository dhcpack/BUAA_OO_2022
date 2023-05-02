import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.HashMap;

public class InputHandler implements Runnable {
    private ElevatorInput elevatorInput;
    private HashMap<Character, Building> buildings;

    public InputHandler(ElevatorInput elevatorInput) {
        this.elevatorInput = elevatorInput;
        this.buildings = new HashMap<>();
    }

    public void addBuilding(Building building) {
        buildings.put(building.getId().charAt(0), building);
    }

    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                for (Building building : buildings.values()) {
                    building.setEnded();
                }
                break;
            } else {
                buildings.get(request.getFromBuilding()).put(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
