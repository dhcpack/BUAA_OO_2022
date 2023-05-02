import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class MyPersonRequest {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    private final PersonRequest personRequest;

    private ArrayList<ArrayList<Elevator>> floorElevators;

    private final ArrayList<Integer> startFloorSeq = new ArrayList<>();
    private final ArrayList<Integer> endFloorSeq = new ArrayList<>();
    private final ArrayList<Integer> startBuildingSeq = new ArrayList<>();
    private final ArrayList<Integer> endBuildingSeq = new ArrayList<>();
    private final ArrayList<String> typeSeq = new ArrayList<>();

    private Integer currentStartFloor;
    private Integer currentEndFloor;
    private Integer currentStartBuilding;
    private Integer currentEndBuilding;
    private String currentType;

    public MyPersonRequest(PersonRequest personRequest) {
        this.personRequest = personRequest;
    }

    public int getPersonId() {
        return personRequest.getPersonId();
    }

    private int getFromFloor() {
        return personRequest.getFromFloor();
    }

    private int getToFloor() {
        return personRequest.getToFloor();
    }

    public int getFromBuilding() {
        return personRequest.getFromBuilding() - 'A';
    }

    public int getToBuilding() {
        return personRequest.getToBuilding() - 'A';
    }

    public int getCurrentStartFloor() {
        return currentStartFloor;
    }

    public int getCurrentEndFloor() {
        return currentEndFloor;
    }

    public int getCurrentStartBuilding() {
        return currentStartBuilding;
    }

    public int getCurrentEndBuilding() {
        return currentEndBuilding;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setFloorElevator(ArrayList<ArrayList<Elevator>> floorElevators) {
        this.floorElevators = floorElevators;
        arrangeTransfer();
    }

    private void arrangeTransfer() {
        // type1: 在同一座，纵向
        if (getFromBuilding() == getToBuilding()) {
            arrangeOneTrip(getFromFloor(), getToFloor(), getFromBuilding(), getToBuilding());
            return;
        }

        // type2: 纵向 + 横向
        for (Elevator elevator : floorElevators.get(getToFloor() - 1)) {
            if (suitableFloorElevator(elevator)) {
                if (!(getFromFloor() == getToFloor())) {  // 直接横向
                    arrangeOneTrip(getFromFloor(), getToFloor(),
                            getFromBuilding(), getFromBuilding());
                }
                arrangeOneTrip(getToFloor(), getToFloor(), getFromBuilding(), getToBuilding());
                return;
            }
        }
        // type3: 横向 + 纵向
        for (Elevator elevator : floorElevators.get(getFromFloor() - 1)) {
            if (suitableFloorElevator(elevator)) {
                arrangeOneTrip(getFromFloor(), getFromFloor(), getFromBuilding(), getToBuilding());
                arrangeOneTrip(getFromFloor(), getToFloor(), getToBuilding(), getToBuilding());
                return;
            }
        }

        Elevator transferElevator = null;
        // type4: 纵向 + 横向 + 纵向  横向在之间
        int iterStart = Math.min(getFromFloor(), getToFloor());
        int iterEnd = Math.max(getFromFloor(), getToFloor());

        for (int floor = iterStart; floor <= iterEnd; floor++) {
            for (Elevator elevator : floorElevators.get(floor - 1)) {
                if (suitableFloorElevator(elevator) && (transferElevator == null ||
                        elevator.getSpeed() < transferElevator.getSpeed())) {  //  找到速度最快的电梯
                    transferElevator = elevator;
                }
            }
        }
        if (transferElevator != null) {
            arrangeThreeTransferTrip(transferElevator);
            return;
        }

        // type5: 纵向 + 横向 + 纵向  横向在两侧
        int distance = 0;
        int currentDistance;

        for (int i = 0; i < 10; i++) {
            currentDistance = Math.abs(i + 1 - getFromFloor()) + Math.abs(
                    i + 1 - getToFloor());  //  找到距离最短的电梯
            if (transferElevator != null && currentDistance > distance) {
                continue;
            }
            for (Elevator elevator : floorElevators.get(i)) {
                if (suitableFloorElevator(elevator)) {
                    transferElevator = elevator;
                }
            }
        }
        assert transferElevator != null;
        arrangeThreeTransferTrip(transferElevator);
    }

    private void arrangeOneTrip(int startFloor, int endFloor, int startBuilding, int endBuilding) {
        startFloorSeq.add(startFloor);
        endFloorSeq.add(endFloor);
        startBuildingSeq.add(startBuilding);
        endBuildingSeq.add(endBuilding);
    }

    private void arrangeThreeTransferTrip(Elevator transferElevator) {
        arrangeOneTrip(getFromFloor(), transferElevator.getCurrentFloor(),
                getFromBuilding(), getFromBuilding());
        arrangeOneTrip(transferElevator.getCurrentFloor(), transferElevator.getCurrentFloor(),
                getFromBuilding(), getToBuilding());
        arrangeOneTrip(transferElevator.getCurrentFloor(), getToFloor(),
                getToBuilding(), getToBuilding());
    }

    public void getOneTrip() {
        this.currentStartFloor = startFloorSeq.remove(0);
        this.currentEndFloor = endFloorSeq.remove(0);
        this.currentStartBuilding = startBuildingSeq.remove(0);
        this.currentEndBuilding = endBuildingSeq.remove(0);
        if (currentStartFloor.equals(currentEndFloor)) {
            currentType = "floor";
        } else {
            currentType = "building";
        }
    }

    private boolean suitableFloorElevator(Elevator elevator) {
        return elevator.canReach(getFromBuilding(), getToBuilding());
    }

    public boolean isSameEndDirection(int direction, int currentFloor, int currentBuilding) {
        // 终点在当前方向上
        if (getCurrentType().equals("building") &&
                ((getCurrentEndFloor() > currentFloor && direction == UP) ||
                        (getCurrentEndFloor() < currentFloor && direction == DOWN))) {
            return true;
        }
        if (getCurrentType().equals("floor") &&
                (((currentBuilding + direction + 5) % 5 == getCurrentEndBuilding()) ||
                        (currentBuilding + direction * 2 + 5) % 5 == getCurrentEndBuilding())) {
            return true;
        }
        return false;
    }

    public boolean isSameStartDirection(int direction, int currentFloor, int currentBuilding) {
        // 起点在当前方向上
        if (getCurrentType().equals("building") &&
                ((getCurrentStartFloor() > currentFloor && direction == UP) ||
                        (getCurrentStartFloor() < currentFloor && direction == DOWN))) {
            return true;
        }
        if (getCurrentType().equals("floor") && (getCurrentStartBuilding() == currentBuilding ||
                (getCurrentStartBuilding() == (currentBuilding + direction + 5) % 5) ||
                (getCurrentStartBuilding() == (currentBuilding + direction * 2 + 5) % 5))) {
            return true;
        }
        return false;
    }

    public boolean isDestination(int currentFloor, int currentBuilding) {
        return getToFloor() == currentFloor && getToBuilding() == currentBuilding;
    }
}
