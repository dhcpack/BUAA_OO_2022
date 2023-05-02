package mymain;

import java.util.HashMap;

public class CalFre {
    private static final CalFre CALCULATOR = new CalFre();
    private HashMap<Integer, Integer> sumCal;
    private HashMap<Integer, HashMap<Integer, Integer>> idCal;

    public static CalFre getInstance() {
        return CALCULATOR;
    }

    public CalFre() {
    }

    public void init() {
        sumCal = new HashMap<>();
        idCal = new HashMap<>();
    }

    public void updateCal(int type, int id, boolean check) {
        if (!sumCal.containsKey(type)) {
            sumCal.put(type, 0);
        }
        if (check == true) {
            sumCal.put(type, sumCal.get(type) + 1);
        }
        if (!idCal.containsKey(id)) {
            idCal.put(id, new HashMap<>());
            for (int i = 0; i < 8; i += 1) {
                idCal.get(id).put(i, 0);
            }
        }
        int preCnt = idCal.get(id).get(type);
        idCal.get(id).put(type, preCnt + 1);
    }

    public int querySum(int type) {
        return sumCal.get(type);
    }

    public int queryId(int type, int id) {
        return idCal.get(id).get(type);
    }
}
