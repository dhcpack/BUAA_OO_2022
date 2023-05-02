
import java.util.ArrayList;

public class Const {
    public static final int OPEN = 1;
    public static final int REST = 0;
    public static final int MOVE = 2;
    public static final int ARRIVE = 3;
    public static final int CLOSE = 4;
    public static final int END = 5;
    public static final int UP = 1;//上 顺时针
    public static final int DOWN = -1;//下 逆时针
    public static final int PROEND = 1;
    public static final int PRONOTEND = 0;
    public static final int MAXNUM_BUILDING = 10;
    public static final int MAXNUM_FLOOR = 5;
    public static final int MINFLOORS = 1;
    public static final char INITBLOCK = 1;
    public static final int CANMAX = 5;
    public static final int FIRST = 0;
    public static final int SECOND = 2;

    public static int distance(PersonRequest2 r, int dir) {

        if (dir == Const.UP) {
            return (r.getToBuilding() - r.getFromBuilding() + 5) % 5;
        } else {
            return 5 - (r.getToBuilding() - r.getFromBuilding() + 5) % 5;
        }
    }

    public static int getdir(PersonRequest2 request) {
        if (Const.distance(request, Const.UP) < Const.distance(request, Const.DOWN)) {
            return Const.UP;
        } else {
            return Const.DOWN;
        }
    }

    public static ArrayList<Integer> getcanArrive(int m) {
        ArrayList<Integer> can = new ArrayList<>();
        for (int i = 0; i <= CANMAX; i++) {
            can.add(0);
        }
        for (int i = 0; i < CANMAX; i++) {
            if (((m >> i) & 1) == 1) {
                can.set(i + 1, 1);
            }
        }
        return can;
    }

    public static void or(ArrayList<Integer> x1, ArrayList<Integer> x2) {
        for (int i = 1; i <= Const.CANMAX; i++) { //浅拷贝
            int x = x1.get(i) | x2.get(i);
            x1.set(i, x);
        }
    }

    public static void and(ArrayList<Integer> x1, ArrayList<Integer> x2) {
        for (int i = 1; i <= Const.CANMAX; i++) { //浅拷贝
            int x = x1.get(i) & x2.get(i);
            x1.set(i, x);
        }
    }

    public static ArrayList<Integer> cancopy(ArrayList<Integer> x1) {
        ArrayList<Integer> x = new ArrayList<>();
        x.add(0);
        for (int i = 1; i <= Const.CANMAX; i++) {
            x.add(x1.get(i));
        }
        return x;
    }

    public static boolean canequal0(ArrayList<Integer> can) {
        for (int i = 1; i <= Const.CANMAX; i++) {
            if (can.get(i) == 1) {
                return false;
            }
        }
        return true;
    }
}
