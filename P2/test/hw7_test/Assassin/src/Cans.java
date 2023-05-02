import java.util.ArrayList;

public class Cans { //共享对象
    private ArrayList<ArrayList<Integer>> cans;

    public Cans() {
        this.cans = new ArrayList<>();
    }

    public synchronized void addcan(ArrayList<Integer> can) {
        cans.add(can);
    }

    public synchronized boolean cansempty() {
        return cans.size() == 0;
    }

    public synchronized ArrayList<Integer> andall() {
        ArrayList<Integer> x0 = Const.cancopy(cans.get(0)); //深拷贝
        for (int i = 1; i < cans.size(); i++) {
            Const.and(x0, cans.get(i));
        }
        return x0;
    }

    public synchronized ArrayList<Integer> orall() {
        ArrayList<Integer> x0 = Const.cancopy(cans.get(0)); //深拷贝
        for (int i = 1; i < cans.size(); i++) {
            Const.or(x0, cans.get(i));
        }
        return x0;
    }

    public synchronized boolean cangetto(int fromblock, int toblock) { //basic
        //System.out.println(cans.size());
        if (cansempty()) {
            return false;
        }
        for (ArrayList<Integer> can : cans) {
            if (can.get(fromblock) == 1 && can.get(toblock) == 1) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean cangetto2(int fromblock, int toblock) {
        if (cansempty()) {
            return false;
        }
        ArrayList<Integer> xand = andall();
        if (Const.canequal0(xand)) {
            return false; //不连通
        }
        //联通
        ArrayList<Integer> xor = orall();
        if (xor.get(fromblock) == 1 && xor.get(toblock) == 1) {
            return true; //可到达
        }
        return false;
    }
}
