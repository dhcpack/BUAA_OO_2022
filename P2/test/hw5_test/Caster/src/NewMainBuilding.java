import com.oocourse.elevator1.PersonRequest;

import java.util.HashMap;

public class NewMainBuilding extends Thread {
    private MyQueue<PersonRequest> inputQueue;
    private HashMap<Character, SubBuilding> subBuildings;

    public NewMainBuilding(MyQueue<PersonRequest> inputQueue) {
        this.setName("NewMainBuilding");

        this.inputQueue = inputQueue;

        subBuildings = new HashMap<>();
        for (char i = 'A'; i <= 'E'; i++) {
            subBuildings.put(i, new SubBuilding(i));
        }
    }

    @Override
    public void run() {
        while (true) {
            if (inputQueue.isEnd() && inputQueue.isEmpty()) {
                setSubBuildingsEnd();
                break;
            }
            PersonRequest person = inputQueue.getOne();
            // System.out.println("NewMainBuilding get " + person);
            if (person == null) {
                continue;
            }
            subBuildings.get(person.getFromBuilding()).addPerson(person);
        }
        //System.out.println(this.getName() + " thread end");
    }

    private void setSubBuildingsEnd() {
        for (SubBuilding value : subBuildings.values()) {
            value.setEnd(true);
        }
    }
}
