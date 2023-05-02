package main;

import java.util.HashMap;

public class Calculator {
    private static final Calculator CALCULATOR = new Calculator();
    private HashMap<Integer, Integer> excCal;
    private HashMap<Integer, HashMap<Integer, Integer>> perCal;
    private HashMap<Integer, HashMap<Integer, Integer>> groupCal;

    private HashMap<Integer, HashMap<Integer, Integer>> messageCal;

    public Calculator() {
    }

    public void init() {
        excCal = new HashMap<>();
        perCal = new HashMap<>();
        groupCal = new HashMap<>();
        messageCal = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            excCal.put(i, 0);
        }
    }

    public static Calculator getInstance() {
        return CALCULATOR;
    }

    public void updateExcCal(int type) {
        excCal.put(type, excCal.get(type) + 1);
    }

    public void updatePerCal(int type, int id) {
        int pre;
        if (!perCal.containsKey(id)) {
            perCal.put(id, new HashMap<>());
            for (int i = 0; i < 4; i++) {
                perCal.get(id).put(i, 0);
            }
        }
        pre = perCal.get(id).get(type);
        perCal.get(id).put(type, pre + 1);
    }

    public void updateGroupCal(int type, int id) {
        if (!groupCal.containsKey(id)) {
            groupCal.put(id, new HashMap<>());
            for (int i = 4; i < 6; i++) {
                groupCal.get(id).put(i, 0);
            }
        }
        int pre = groupCal.get(id).get(type);
        groupCal.get(id).put(type, pre + 1);
    }

    public void updateMessageCal(int type, int id) {
        if (!messageCal.containsKey(id)) {
            messageCal.put(id, new HashMap<>());
            for (int i = 6; i < 8; i++) {
                messageCal.get(id).put(i, 0);
            }
        }
        int pre = messageCal.get(id).get(type);
        messageCal.get(id).put(type, pre + 1);
    }

    public int getExcCal(int type) {
        return excCal.get(type);
    }

    public int getPerCal(int type, int id) {
        return perCal.get(id).get(type);
    }

    public int getGroupCal(int type, int id) {
        return groupCal.get(id).get(type);
    }

    public int getMessageCal(int type, int id) {
        return messageCal.get(id).get(type);
    }
}
