package tasktwo;

import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import taskone.Elevator;
import taskone.Output;
import taskone.RequestQueue;
import taskone.WaitTable;
import taskthree.PersonLink;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class Controller {
    private Output output;
    private ArrayList<WidthQueue> widthQueues;
    private ArrayList<RequestQueue> requestQueues;
    private ArrayList<Elevator> elevators = new ArrayList<>();
    private ArrayList<WidthElevator> widthElevators = new ArrayList<>();
    private final int widthLength = 5;
    private final int lenth = 10;
    private int [] numOfE = new int[5];
    private int [] numOfW = new int[10];
    private ArrayList<WidthTable> widthTables = new ArrayList<>();
    private ArrayList<WaitTable> waitTables = new ArrayList<>();
    private ExecutorService exec;
    private int [] numOfPer = new int[10];

    public Controller(ExecutorService exec, ArrayList<WidthQueue> floors,
                      ArrayList<RequestQueue> buildings, Output output) {
        this.output = output;
        this.exec = exec;
        this.requestQueues = buildings;
        this.widthQueues = floors;
        for (int i = 0; i < 5; i++) {
            waitTables.add(new WaitTable());
        }
        for (int i = 0; i < 10; i++) {
            widthTables.add(new WidthTable());
        }
        for (int i = 0; i < 5; i++) {
            numOfE[i]++;
            Elevator elevator = new Elevator(buildings.get(i), i + 1, output,
                    (char)('A' + i), waitTables.get(i), 600, 8, this);
            elevators.add(elevator);
            exec.execute(elevator);
        }
        WidthElevator elevator = new WidthElevator(widthQueues.get(0), 6, output, 1,
                widthTables.get(0), 600, 8, 31, this);
        widthElevators.add(elevator);
        numOfW[0]++;
        exec.execute(elevator);
    }

    private synchronized boolean isEmpty() {
        for (int i : numOfPer) {
            if (i > 0) { return false; }
        }
        return true;
    }

    private void cancel(PersonRequest personRe) {
        try {
            for (Elevator elevator : elevators) {
                elevator.setCancel(true);
            }
            for (WidthElevator widthElevator : widthElevators) {
                widthElevator.setCancel(true);
            }
            int k = 0;
            for (WidthQueue widthQueue : widthQueues) {
                for (int i = 0; i < numOfW[k]; i++) {
                    widthQueue.put(personRe);
                }
                k++;
            }
            k = 0;
            for (RequestQueue requestQueue : requestQueues) {
                for (int i = 0; i < numOfE[k]; i++) {
                    requestQueue.put(personRe);
                }
                k++;
            }
            exec.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void split(PersonRequest personRe) {
        int max = Integer.MAX_VALUE;
        int floor = 1;
        for (WidthElevator elevator : widthElevators) {
            if (elevator.canArrive(personRe.getFromBuilding(), personRe.getToBuilding())) {
                int temp = elevator.getCrowding(personRe.getFromFloor(),
                        personRe.getToFloor(), numOfPer);
                if (temp < max) {
                    max = temp;
                    floor = elevator.getFloor();
                }
            }
        }
        numOfPer[floor - 1]++;
        PersonLink personLi;
        if (floor == personRe.getFromFloor()) {
            personLi = new PersonLink(personRe, floor, personRe.getToBuilding());
            personLi.setState(PersonLink.WIDTH);
        } else {
            personLi = new PersonLink(personRe, floor, personRe.getFromBuilding());
            personLi.setState(PersonLink.VERTICAL);
        }
        put(personLi);
    }

    private void put(PersonLink personLi) {
        try {
            if (personLi.getState() == PersonLink.WIDTH) {
                widthQueues.get(personLi.getFromFloor() - 1).put(personLi);
            } else {
                requestQueues.get(personLi.getFromBuilding() - 'A').put(personLi);
            }
            if (personLi.isNoTrs()) {
                synchronized (this) {
                    numOfPer[personLi.getFromFloor() - 1]--;
                    if (numOfPer[personLi.getFromFloor() - 1] == 0) {
                        notifyAll();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(PersonLink personLi) {
        if (personLi.transfer()) {
            put(personLi);
        }
    }

    public void add(Request request) {
        if (request instanceof PersonRequest) {
            try {
                PersonRequest personRe = (PersonRequest) request;
                if (personRe.getToFloor() == 0) {
                    synchronized (this) {
                        while (!isEmpty()) {
                            wait();
                        }
                    }
                    cancel(personRe);
                    return;
                }
                if (personRe.getFromBuilding() == personRe.getToBuilding()) {
                    requestQueues.get(personRe.getFromBuilding() - 'A').put(personRe);
                } else {
                    split(personRe);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            ElevatorRequest eleRe = (ElevatorRequest) request;
            if (eleRe.getType().equals("floor")) {
                WidthElevator elevator = new WidthElevator(widthQueues.get(eleRe.getFloor() - 1),
                        eleRe.getElevatorId(), output, eleRe.getFloor(),
                        widthTables.get(eleRe.getFloor() - 1), (int)(eleRe.getSpeed() * 1000),
                        eleRe.getCapacity(), eleRe.getSwitchInfo(), this);
                numOfW[eleRe.getFloor() - 1]++;
                widthElevators.add(elevator);
                exec.execute(elevator);
            } else {
                Elevator elevator = new Elevator(requestQueues.get(eleRe.getBuilding() - 'A'),
                        eleRe.getElevatorId(), output, eleRe.getBuilding(),
                        waitTables.get(eleRe.getBuilding() - 'A'),
                        (int)(eleRe.getSpeed() * 1000), eleRe.getCapacity(), this);
                numOfE[eleRe.getBuilding() - 'A']++;
                elevators.add(elevator);
                exec.execute(elevator);
            }
        }
    }
}
