import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Vector;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        ArrayList<RequestQueue> allBuildingList = new ArrayList<>();
        Vector<RequestQueue> allCircleList = new Vector<>();
        Vector<Thread> allElevator = new Vector<>();
        RequestQueue changeList = new RequestQueue();
        RequestQueue total = new RequestQueue();

        int[] hasCircleBuilding = new int[10];

        for (int i = 0; i < 10; i++) {
            allCircleList.add(new RequestQueue());
        }

        for (int i = 0; i < 5; i++) {
            RequestQueue parallelQueue = new RequestQueue();
            Elevator elevator = new Elevator(i + 1, (char) ((int) 'A' + i), 1
                    , parallelQueue, allCircleList,
                    changeList);
            allBuildingList.add(parallelQueue);
            allElevator.add(elevator);
            elevator.setName("ele " + (i + 1));
            elevator.start();
        }

        MainScheduler scheduler = new MainScheduler(total, allBuildingList,
                allCircleList, hasCircleBuilding, allElevator, changeList);
        scheduler.setName("Main Scheduler");
        scheduler.start();

        Input input = new Input(total, allBuildingList, allCircleList,
                hasCircleBuilding, allElevator, changeList);
        input.setName("input");
        input.start();

    }
}
/*
ADD-floor-10-7
1-FROM-C-2-TO-A-5
2-FROM-D-3-TO-E-1
* */