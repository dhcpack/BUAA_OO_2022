import java.util.HashMap;

public class BuildingQueues {
    private final HashMap<Integer, MyQueue<MyPerson>> horiQueues = new HashMap<>();
    private final HashMap<Character, MyQueue<MyPerson>> vertQueues = new HashMap<>();

    public BuildingQueues(MySem sem) {
        for (int i = 1; i <= 10; i++) {
            horiQueues.put(i, new MyQueue<>(sem));
        }
        for (char c : "ABCDE".toCharArray()) {
            vertQueues.put(c, new MyQueue<>(sem));
        }
    }

    public void end() {
        for (MyQueue<MyPerson> q : horiQueues.values()) {
            q.end();
        }
        for (MyQueue<MyPerson> q : vertQueues.values()) {
            q.end();
        }
    }

    public HashMap<Integer, MyQueue<MyPerson>> getHoriQueues() {
        return horiQueues;
    }

    public HashMap<Character, MyQueue<MyPerson>> getVertQueues() {
        return vertQueues;
    }

    public void putIntoQueues(MyPerson person) {
        if (person.getCurrBuilding() == person.getNextBuilding()) {
            // vertical
            vertQueues.get(person.getCurrBuilding()).put(person);
        } else {
            // horizontal
            horiQueues.get(person.getCurrFloor()).put(person);
        }
    }

    public void personNext(MyPerson person) {
        if (person.stepDone()) {
            putIntoQueues(person);
        }
    }
}
