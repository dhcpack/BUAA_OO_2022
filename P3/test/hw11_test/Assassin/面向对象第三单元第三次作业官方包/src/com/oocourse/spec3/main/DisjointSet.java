package com.oocourse.spec3.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DisjointSet {
    private HashMap<Person, Person> father = new HashMap<>();
    private HashMap<Person, Integer> rank = new HashMap<>();
    private HashMap<Person, Block> blocks = new HashMap<>();

    public DisjointSet() {
    }

    public Integer getBlockSum() {
        return blocks.size();
    }

    public Person find(Person item) {
        Person temp = item;
        ArrayList<Person> tempK = new ArrayList<>();
        while (!father.get(temp).equals(temp)) {
            temp = father.get(temp);
            tempK.add(temp);
        }
        for (Person item1 : tempK) {
            father.put(item1, temp);
        }
        return temp;
    }

    public void merge(Person k1, Person k2) {
        Person x = find(k1);
        Person y = find(k2);
        boolean flag = blocks.get(x).getSign() == 0 && blocks.get(y).getSign() == 0;
        int value = blocks.get(x).getLeastValue() + blocks.get(y).getLeastValue()
                + k1.queryValue(k2);
        if (rank.get(x) <= rank.get(y)) {
            mergeExtracted(x, y, flag, value);
        } else {
            mergeExtracted(y, x, flag, value);
        }
        if (rank.get(x).equals(rank.get(y)) && !x.equals(y)) {
            int temp = rank.get(y);
            rank.put(y, temp + 1);
        }
    }

    private void mergeExtracted(Person small, Person large, boolean flag, int value) {
        father.put(small, large);
        if (!small.equals(large)) {
            blocks.get(large).addAll(blocks.get(small).getItems());
            blocks.remove(small);
        }
        blocks.get(large).setSign(1);
        if (flag && !small.equals(large)) {
            blocks.get(large).setLeastValue(value);
            blocks.get(large).setSign(0);
        }
    }

    public void addItem(Person k) {
        father.put(k, k);
        rank.put(k, 1);
        HashSet<Person> hashSet = new HashSet<>();
        hashSet.add(k);
        Block block = new Block(hashSet, k, 1);
        blocks.put(k, block);
    }

    public Block getBlock(Person k) {
        return blocks.get(find(k));
    }

    public boolean isCircle(Person k1, Person k2) {
        return find(k1).equals(find(k2));
    }

    public void addAll(HashSet<Person> hashSet) {
        for (Person k : hashSet) {
            addItem(k);
        }
    }

}
