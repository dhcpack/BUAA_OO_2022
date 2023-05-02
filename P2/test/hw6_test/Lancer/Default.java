import java.util.ArrayList;
import java.util.Arrays;

public class Default {
    private static final ArrayList<Integer> REACHABLE_VER =
            new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    private static final ArrayList<Integer> REACHABLE_HORI =
            new ArrayList<Integer>(Arrays.asList(65, 66, 67, 68, 69));
    private static final ArrayList<Integer> STOPPABLE_VER =
            new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    private static final ArrayList<Integer> STOPPABLE_HORI =
            new ArrayList<Integer>(Arrays.asList(65, 66, 67, 68, 69));
    private static final int CAPACITY = 6;
    private static final int MOVE_PER_FLOOR_VER = 400;
    private static final int MOVE_PER_FLOOR_HORI = 200;
    private static final int OPEN_TIME = 200;
    private static final int CLOSE_TIME = 200;
    private static final int BEGIN_FLOOR = 1;

    public static VerElevator defaultVerElevator(int num, char building) {
        return new VerElevator(REACHABLE_VER, STOPPABLE_VER, num, CAPACITY,
                building, 1, MOVE_PER_FLOOR_VER,
                OPEN_TIME, CLOSE_TIME, 1);
    }

    public static HoriElevator defaultHoriElevator(int num, int floor) {
        return new HoriElevator(REACHABLE_HORI, STOPPABLE_HORI,
                num, CAPACITY, 'A', floor,
                MOVE_PER_FLOOR_HORI, OPEN_TIME, CLOSE_TIME, 'A');
    }
}
