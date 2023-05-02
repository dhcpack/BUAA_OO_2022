import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class TransferMap {
    private static final TransferMap TRANSFER_MAP = new TransferMap();
    private HashMap<Integer, ArrayList<Integer>> floorInfo = new HashMap<>();

    //private int[][] floorElevatorCnt = new int[11][3];
    //private int[][] buildingElevatorCnt = new int[5][3];

    TransferMap() {
        for (int i = 1; i <= 10; i++) {
            floorInfo.put(i, new ArrayList<>());
        }
        floorInfo.get(1).add(31);
        /*
        floorElevatorCnt[1][0]++;
        for (int i = 0; i < 5; i++) {
            buildingElevatorCnt[i][0]++;
        }

         */
    }

    public static TransferMap getInstance() {
        return TRANSFER_MAP;
    }

    public synchronized void add(int floor, int switchInfo) {
        floorInfo.get(floor).add(switchInfo);
    }

    /*
    public synchronized void addCnt(boolean isBuilding, char building, int floor, int type) {
        if (isBuilding) {
            buildingElevatorCnt[building - 'A'][type]++;
        } else {
            floorElevatorCnt[floor][type]++;
        }
    }

     */

    public synchronized Person split(Person person) {
        char fromBuilding = person.getFromBuilding();
        char toBuilding = person.getToBuilding();
        int fromFloor = person.getFromFloor();
        int toFloor = person.getToFloor();

        if (fromBuilding == toBuilding) {
            return person;
        }

        int cost = 0x3f3f3f3f;
        // double cost = 1e10;
        int pos = 0;

        for (int i = 1; i <= 10; i++) {
            for (int switchInfo : floorInfo.get(i)) {
                if (isAccessible(switchInfo, fromBuilding, toBuilding)) {
                    /*
                    if (plusCalcCost(fromBuilding, fromFloor, toBuilding, toFloor, i) < cost) {
                        cost = plusCalcCost(fromBuilding, fromFloor, toBuilding, toFloor, i);
                        pos = i;
                    }
                    */
                    if (calcCost(fromFloor, toFloor, i) < cost ||
                            (calcCost(fromFloor, toFloor, i) == cost)
                                    && (i == fromFloor || i == toFloor)) {
                        cost = calcCost(fromFloor, toFloor, i);
                        pos = i;
                    }
                    break;
                }
            }
        }

        ArrayList<Person> nxt = new ArrayList<>();
        if (person.getFromFloor() != pos) {
            nxt.add(new Person(person.getPersonId(), person.getFromBuilding(),
                    person.getFromFloor(), person.getFromBuilding(), pos, nxt));
        }
        nxt.add(new Person(person.getPersonId(), person.getFromBuilding(),
                pos, person.getToBuilding(), pos, nxt));
        if (person.getToFloor() != pos) {
            nxt.add(new Person(person.getPersonId(), person.getToBuilding(),
                    pos, person.getToBuilding(), person.getToFloor(), nxt));
        }
        Person res = nxt.get(0);
        nxt.remove(0);
        return res;
    }

    private boolean isAccessible(int switchInfo, char from, char to) {
        return (((switchInfo >> (from - 'A')) & 1) == 1) && (((switchInfo >> (to - 'A')) & 1) == 1);
    }

    private int calcCost(int fromFloor, int toFloor, int viaFloor) {
        return abs(fromFloor - viaFloor) + abs(toFloor - viaFloor);
    }

    /*
    private double plusCalcCost(char fromBuilding, int fromFloor, char toBuilding,
     int toFloor, int viaFloor) {
        return calcBuilding(fromBuilding, abs(fromFloor - viaFloor)) +
                calcFloor(viaFloor) +
                calcBuilding(toBuilding, abs(toFloor - viaFloor));
    }

    private double calcBuilding(char building, int moveFloor) {
        int k = building - 'A';
        double res = 10800.0 / calc3(buildingElevatorCnt[k], 6, 3, 2);
        res += moveFloor * 120000.0 * calc3(buildingElevatorCnt[k], 6, 3, 2) /
                calc3(buildingElevatorCnt[k], 36, 9, 4);
        if (moveFloor == 0) {
            res -= 1e8;
        }
        return res;
    }

    private double calcFloor(int floor) {
        double res = 2400.0 / calc3(floorElevatorCnt[floor], 6, 3, 2);
        res += 3000.0 * calc3(floorElevatorCnt[floor], 6, 3, 2) /
                calc3(floorElevatorCnt[floor], 36, 9, 4);
        return res;
    }

    private double calc3(int[] a, int x, int y, int z) {
        return (double)(a[0] * x + a[1] * y + a[2] * z + 1e-7);
    }
    */

}
