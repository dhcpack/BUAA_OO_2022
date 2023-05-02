import java.util.ArrayList;
import java.util.HashMap;
import com.oocourse.elevator3.PersonRequest;

public class MidFinder {
    private final HashMap<Integer, ArrayList<SwitchInfo>> hm = new HashMap<>();

    public MidFinder(int floors) {
        for (int i = 1; i <= floors; i++) {
            hm.put(i, new ArrayList<>());
        }
    }

    public void addSwitchInfo(int floor, SwitchInfo switchInfo) {
        hm.get(floor).add(switchInfo);
    }

    public int findMidFloor(PersonRequest pReq) {
        int floorFr = pReq.getFromFloor();
        int floorTo = pReq.getToFloor();
        char buildingFr = pReq.getFromBuilding();
        char buildingTo = pReq.getToBuilding();
        int[] midFloor = {1};
        int[] minValue = {Integer.MAX_VALUE};
        hm.forEach((floor, infos) -> {
            boolean[] canCarry = {false};
            infos.forEach(switchInfo -> {
                if (switchInfo.canOpenAt(buildingFr) && switchInfo.canOpenAt(buildingTo)) {
                    canCarry[0] = true;
                }
            });
            if (canCarry[0]) {
                int value = Math.abs(floorFr - floor) + Math.abs(floorTo - floor);
                if (minValue[0] > value) {
                    minValue[0] = value;
                    midFloor[0] = floor;
                }
            }
        });
        return midFloor[0];
    }
}
