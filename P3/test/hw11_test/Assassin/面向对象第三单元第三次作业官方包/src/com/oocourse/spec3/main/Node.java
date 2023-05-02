package com.oocourse.spec3.main;

public class Node {
    private final int id;
    private final int distance;

    public Node(int id, int distance) {
        this.id = id;
        this.distance = distance;
    }

    public int getId() {
        return this.id;
    }

    public int getDistance() {
        return this.distance;
    }
}

