import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        TowerReqQueue[] towerQueues = new TowerReqQueue[5];
        FloorReqQueue[] floorReqQueues = new FloorReqQueue[10];
        ReqMaker reqMaker = new ReqMaker(towerQueues, floorReqQueues);
        reqMaker.start();
        char tower = 'A';
        FloorElevator[] floorElevators = new FloorElevator[5];
        for (int i = 0; i < 10; i++) {
            floorReqQueues[i] = new FloorReqQueue(i + 1);
        }
        for (int i = 0; i < 5; i++) {
            towerQueues[i] = new TowerReqQueue((char) (tower + i));
            floorElevators[i] = new FloorElevator(i + 1, (char) (tower + i), towerQueues[i]);
            floorElevators[i].start();
        }
    }
}
